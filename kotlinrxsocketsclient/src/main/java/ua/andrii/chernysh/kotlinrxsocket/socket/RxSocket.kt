package ua.andrii.chernysh.kotlinrxsocket.socket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.reactivestreams.Subscription
import ua.andrii.chernysh.kotlinrxsocket.exception.EmptySocketDataException
import ua.andrii.chernysh.kotlinrxsocket.exception.EventAlreadySubscribedException
import ua.andrii.chernysh.kotlinrxsocket.exception.EventJsonSyntaxException
import ua.andrii.chernysh.kotlinrxsocket.socket.RxSocketEvent.*
import java.io.Closeable
import java.util.*

/**
 * Copyright 2018. Andrii Chernysh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/**
 * Base RX socket client
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 */
class RxSocket(hostIp: String, port: Int,
               namespace: String,
               options: IO.Options? = null,
               private val gson: Gson,
               private val socketLoggingInterceptor: SocketLoggingInterceptor?) : Closeable {

    private val socket: Socket
    private val socketEvents = mutableListOf<String>()                // Names of events
    private var compositeDisposable = CompositeDisposable()           // For observable
    private val compositeSubscription = mutableListOf<Subscription>() // For flowable
    private val systemSubjects = mutableMapOf<String, PublishSubject<Any>>() // For SENDING system messages
    private val observables = hashMapOf<String, Observable<*>>()
    private val flowables = hashMapOf<String, Flowable<*>>()

    init {
        Log.d("TAG", "Connecting to $hostIp:$port/$namespace")
        socket = if (options == null) {
            IO.socket("$hostIp:$port/$namespace")
        } else {
            IO.socket("$hostIp:$port/$namespace", options)
        }

        systemSubjects += SEND_DATA_ERROR to PublishSubject.create()
    }

    fun connect() {
        socket.connect()
    }

    override fun close() {
        if (socket.connected()) {
            socket.disconnect()
        }

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        compositeDisposable = CompositeDisposable()

        for (subscription in compositeSubscription) {
            subscription.cancel()
        }
        compositeSubscription.clear()

        for (event in socketEvents) {
            socket.off(event)
        }
    }

    fun <T> sendData(eventName: String, vararg data: T) {
        if (socket.connected()) {
            socketLoggingInterceptor?.logInfo("RxSocket. Custom event $eventName. Send data: ${Arrays.toString(data)}")
            socket.emit(eventName, gson.toJson(data))
        } else {
            socketLoggingInterceptor?.logError("RxSocket. Custom event $eventName." +
                    " Error while sending data: ${Arrays.toString(data)}. Socket is disconnected.")
            systemSubjects[SEND_DATA_ERROR]!!.onNext(Unit)
        }
    }

    fun <T> sendData(eventName: String,
                     vararg data: T,
                     acknowledgment: (args: Array<out Any>) -> Unit) {
        if (socket.connected()) {
            socketLoggingInterceptor?.logInfo("RxSocket. Custom event $eventName. Send data with acknowledgement: ${Arrays.toString(data)}")
            val sendData = data.map { gson.toJson(data) }.toTypedArray()
            socket.emit(eventName, sendData) { acknowledgment(it) }
        } else {
            socketLoggingInterceptor?.logError("RxSocket. Custom event $eventName." +
                    " Error while sending data: ${Arrays.toString(data)} with acknowledgement. Socket is disconnected.")
            systemSubjects[SEND_DATA_ERROR]!!.onNext(Unit)
        }
    }

    fun <T : Any> flowableOn(eventName: String, returnClass: Class<T>,
                             backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP): Flowable<T> {
        val flowable = flowables.getOrElse(eventName) {
            Flowable.create<T>({ emitter ->
                val listener = getEmitterListener<T>(emitter, eventName, returnClass)
                socket.on(eventName, listener)
                socketEvents.add(eventName)
            }, backpressureStrategy)
                    .doOnSubscribe {
                        compositeSubscription.add(it)
                    }
        }
        flowables[eventName] = flowable

        return flowable as Flowable<T>
    }

    fun <T : Any> observableOn(eventName: String, returnClass: Class<T>): Observable<T> {
        val observable = observables.getOrElse(eventName) {
            Observable.create<T> { emitter ->
                val listener = getEmitterListener<T>(emitter, eventName, returnClass)
                socket.on(eventName, listener)
                socketEvents.add(eventName)
            }.doOnSubscribe {
                if (!it.isDisposed) {
                    compositeDisposable.add(it)
                }
            }
        }
        observables[eventName] = observable

        return observable as Observable<T>
    }

    private fun <T : Any> getEmitterListener(
            emitter: io.reactivex.Emitter<T>,
            eventName: String, returnClass: Class<T>) =

            Emitter.Listener { args ->
                if (args == null || args[0] == null) {
                    socketLoggingInterceptor?.logError("RxSocket. " +
                            "Custom event $eventName: an error occurred when receiving data." +
                            " Empty input args array.")
                    emitter.onError(EmptySocketDataException(eventName))
                } else {
                    try {
                        val data = gson.fromJson<T>(args[0].toString(), returnClass)
                        socketLoggingInterceptor?.logInfo("RxSocket. Custom event $eventName. Data: $data")
                        emitter.onNext(data)
                    } catch (e: JsonSyntaxException) {
                        socketLoggingInterceptor?.logError("RxSocket." +
                                " Custom event $eventName: an error occurred when receiving data." +
                                " Json syntax exception. Message: ${e.message}")
                        emitter.onError(EventJsonSyntaxException(eventName, e.message))
                    }
                }
            }

    fun observableOnConnect() = systemSocketEventObservable(Socket.EVENT_CONNECT)
    fun observableOnConnecting() = systemSocketEventObservable(Socket.EVENT_CONNECTING)
    fun observableOnConnectError() = systemSocketEventErrorObservable(Socket.EVENT_CONNECT_ERROR)
    fun observableOnConnectTimeout() = systemSocketEventObservable(Socket.EVENT_CONNECT_TIMEOUT)
    fun observableOnDisconnect() = systemSocketEventObservable(Socket.EVENT_DISCONNECT)
    fun observableOnError() = systemSocketEventErrorObservable(Socket.EVENT_ERROR)
    fun observableOnMessage() = systemSocketEventObservable(Socket.EVENT_MESSAGE)
    fun observableOnPing() = systemSocketEventObservable(Socket.EVENT_PING)
    fun observableOnPong() = systemSocketEventObservable(Socket.EVENT_PONG)
    fun observableOnReconnect() = systemSocketEventObservable(Socket.EVENT_RECONNECT)
    fun observableOnReconnecting() = systemSocketEventObservable(Socket.EVENT_RECONNECTING)
    fun observableOnReconnectAttempt() = systemSocketEventObservable(Socket.EVENT_RECONNECT_ATTEMPT)
    fun observableOnReconnectError() = systemSocketEventErrorObservable(Socket.EVENT_RECONNECT_ERROR)
    fun observableOnReconnectFailed() = systemSocketEventObservable(Socket.EVENT_RECONNECT_FAILED)
    fun observableOnSendDataError() = systemSubjects[SEND_DATA_ERROR]

    fun observableOnGenericEvent() =
            Observable.merge(listOf(
                    observableOnConnect().map { CONNECTED },
                    observableOnConnecting().map { CONNECTING },
                    observableOnConnectError().map { CONNECT_ERROR },
                    observableOnConnectTimeout().map { CONNECT_TIMEOUT },
                    observableOnDisconnect().map { DISCONNECTED },
                    observableOnError().map { ERROR },
                    observableOnMessage().map { MESSAGE },
                    observableOnPing().map { PING },
                    observableOnPong().map { PONG },
                    observableOnReconnect().map { RECONNECTED },
                    observableOnReconnecting().map { RECONNECTING },
                    observableOnReconnectAttempt().map { RECONNECT_ATTEMPT },
                    observableOnReconnectError().map { RECONNECT_ERROR },
                    observableOnReconnectFailed().map { RECONNECT_FAILED },
                    observableOnSendDataError()?.map { RxSocketEvent.SEND_DATA_ERROR }))

    fun flowableOnConnect(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(Socket.EVENT_CONNECT, backpressureStrategy)

    fun flowableOnConnecting(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(Socket.EVENT_CONNECTING, backpressureStrategy)

    fun flowableOnConnectError(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventErrorFlowable(Socket.EVENT_CONNECT_ERROR, backpressureStrategy)

    fun flowableOnConnectTimeout(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(Socket.EVENT_CONNECT_TIMEOUT, backpressureStrategy)

    fun flowableOnDisconnect(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(Socket.EVENT_DISCONNECT, backpressureStrategy)

    fun flowableOnError(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventErrorFlowable(Socket.EVENT_ERROR, backpressureStrategy)

    fun flowableOnMessage(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(Socket.EVENT_MESSAGE, backpressureStrategy)

    fun flowableOnPing(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(Socket.EVENT_PING, backpressureStrategy)

    fun flowableOnPong(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(Socket.EVENT_PONG, backpressureStrategy)

    fun flowableOnReconnect(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(Socket.EVENT_RECONNECT, backpressureStrategy)

    fun flowableOnReconnecting(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(Socket.EVENT_RECONNECTING, backpressureStrategy)

    fun flowableOnReconnectAttempt(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(Socket.EVENT_RECONNECT_ATTEMPT, backpressureStrategy)

    fun flowableOnReconnectError(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventErrorFlowable(Socket.EVENT_RECONNECT_ERROR, backpressureStrategy)

    fun flowableOnReconnectFailed(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(Socket.EVENT_RECONNECT_FAILED, backpressureStrategy)

    fun flowableOnSendDataError(backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP) =
            systemSocketEventFlowable(SEND_DATA_ERROR, backpressureStrategy)

    fun flowableOnGenericEvent() =
            Flowable.merge(listOf(
                    flowableOnConnect().map { CONNECTED },
                    flowableOnConnecting().map { CONNECTING },
                    flowableOnConnectError().map { CONNECT_ERROR },
                    flowableOnConnectTimeout().map { CONNECT_TIMEOUT },
                    flowableOnDisconnect().map { DISCONNECTED },
                    flowableOnError().map { ERROR },
                    flowableOnMessage().map { MESSAGE },
                    flowableOnPing().map { PING },
                    flowableOnPong().map { PONG },
                    flowableOnReconnect().map { RECONNECTED },
                    flowableOnReconnecting().map { RECONNECTING },
                    flowableOnReconnectAttempt().map { RECONNECT_ATTEMPT },
                    flowableOnReconnectError().map { RECONNECT_ERROR },
                    flowableOnReconnectFailed().map { RECONNECT_FAILED }))

    private fun systemSocketEventObservable(eventName: String): Observable<Unit> {
        val observable = observables.getOrElse(eventName) {
            Observable.create<Unit> { emitter ->
                val listener = Emitter.Listener { args ->
                    socketLoggingInterceptor?.logInfo("RxSocket. System event $eventName: has fired")
                    emitter.onNext(Unit)
                }
                socket.on(eventName, listener)
                socketEvents.add(eventName)
            }.doOnSubscribe {
                if (!it.isDisposed) {
                    compositeDisposable.add(it)
                }
            }
        }
        observables[eventName] = observable
        return observable as Observable<Unit>
    }

    private fun systemSocketEventErrorObservable(eventName: String): Observable<String> {
        val observable = observables.getOrElse(eventName) {
            Observable.create<String> { emitter ->
                val listener = Emitter.Listener { args ->
                    if (args == null) {
                        socketLoggingInterceptor?.logError("RxSocket. " +
                                "System error event $eventName: has fired.")
                        emitter.onNext("null");
                    } else {
                        socketLoggingInterceptor?.logError("RxSocket. " +
                                "System error event $eventName: has fired. Error message: ${Arrays.toString(args)}")
                        emitter.onNext(Arrays.toString(args));
                    }
                }
                socket.on(eventName, listener)
                socketEvents.add(eventName)
            }.doOnSubscribe {
                if (!it.isDisposed) {
                    compositeDisposable.add(it)
                }
            }
        }
        observables[eventName] = observable

        return observable as Observable<String>
    }

    private fun systemSocketEventFlowable(eventName: String, backpressureStrategy: BackpressureStrategy): Flowable<Unit> {
        val flowable = flowables.getOrElse(eventName) {
            Flowable.create<Unit>({ emitter ->
                val listener = Emitter.Listener { _ ->
                    socketLoggingInterceptor?.logInfo("RxSocket. System event $eventName: has fired")
                    emitter.onNext(Unit)
                }
                socket.on(eventName, listener)
                socketEvents.add(eventName)
            }, backpressureStrategy)
                    .doOnSubscribe {
                        compositeSubscription.add(it)
                    }
        }
        flowables[eventName] = flowable

        return flowable as Flowable<Unit>
    }

    private fun systemSocketEventErrorFlowable(eventName: String, backpressureStrategy: BackpressureStrategy): Flowable<String> {
        val flowable = flowables.getOrElse(eventName) {
            Flowable.create<String>({ emitter ->
                val listener = Emitter.Listener { args ->
                    if (args == null) {
                        socketLoggingInterceptor?.logError("RxSocket. System error event $eventName: has fired.")
                        emitter.onNext("null");
                    } else {
                        socketLoggingInterceptor?.logError("RxSocket. System error event $eventName: has fired." +
                                " Error message: ${Arrays.toString(args)}")
                        emitter.onNext(Arrays.toString(args));
                    }
                }
                socket.on(eventName, listener)
                socketEvents.add(eventName)
            }, backpressureStrategy)
                    .doOnSubscribe {
                        compositeSubscription.add(it)
                    }
        }
        flowables[eventName] = flowable

        return flowable as Flowable<String>
    }

    companion object {
        const val SEND_DATA_ERROR = "Socket.SEND_DATA_ERROR"
    }
}

fun createRxSocket(block: RxSocketBuilder.() -> Unit) = RxSocketBuilder().apply(block).build()

inline fun RxSocket.use(block: RxSocket.() -> Unit) {
    this.connect()
    apply(block)
    this.close()
}
