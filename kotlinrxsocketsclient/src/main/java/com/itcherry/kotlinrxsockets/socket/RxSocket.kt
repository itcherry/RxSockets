package com.itcherry.kotlinrxsockets.socket

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.reactivestreams.Subscription
import com.itcherry.kotlinrxsockets.exception.EmptySocketDataException
import com.itcherry.kotlinrxsockets.exception.EventAlreadySubscribedException
import com.itcherry.kotlinrxsockets.exception.EventJsonSyntaxException
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
 *         Developed by <u>KPI student</u>
 */
class RxSocket(hostIp: String, port: Int,
               namespace: String,
               options: IO.Options? = null,
               private val gson: Gson) {

    private val socket: Socket
    private val socketEvents = mutableListOf<String>()
    private var compositeDisposable = CompositeDisposable()
    private val compositeSubscription = mutableListOf<Subscription>()

    init {
        socket = if (options == null) {
            IO.socket("$hostIp:$port/$namespace")
        } else {
            IO.socket("$hostIp:$port/$namespace", options)
        }
    }

    fun connect() {
        socket.connect()
    }

    fun disconnect() {
        if (socket.connected()) {
            socket.disconnect()

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
    }

    fun <T> sendData(eventName: String, data: T){
        if (socket.connected()) {
            socket.emit(eventName, data);
        }
    }

    fun <T : Any> flowableOn(eventName: String, returnClass: Class<T>,
                             backpressureStrategy: BackpressureStrategy = BackpressureStrategy.DROP): Flowable<T> {
        checkSubscribedToEvent(eventName)
        return Flowable.create<T>({ emitter ->
            val listener = getEmitterListener<T>(emitter, eventName, returnClass)
            socket.on(eventName, listener)
            socketEvents.add(eventName)
        }, backpressureStrategy)
                .doOnSubscribe {
                    compositeSubscription.add(it)
                }
    }

    fun <T : Any> observableOn(eventName: String, returnClass: Class<T>): Observable<T> {
        checkSubscribedToEvent(eventName)
        return Observable.create<T> { emitter ->
            val listener = getEmitterListener<T>(emitter, eventName, returnClass)
            socket.on(eventName, listener)
            socketEvents.add(eventName)
        }.doOnSubscribe {
            if (!it.isDisposed) {
                compositeDisposable.add(it)
            }
        }
    }

    private fun <T : Any> getEmitterListener(emitter: io.reactivex.Emitter<T>,
                                             eventName: String, returnClass: Class<T>) =
            Emitter.Listener { args ->
                if (args == null || args[0] == null) {
                    emitter.onError(EmptySocketDataException(eventName))
                } else {
                    try {
                        val data = gson.fromJson<T>(args[0].toString(), returnClass)
                        emitter.onNext(data)
                    } catch (e: JsonSyntaxException) {
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

    private fun systemSocketEventObservable(eventName: String): Observable<Unit> {
        checkSubscribedToEvent(eventName)
        return Observable.create<Unit> { emitter ->
            val listener = Emitter.Listener { args ->
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

    private fun systemSocketEventErrorObservable(eventName: String): Observable<String> {
        checkSubscribedToEvent(eventName)
        return Observable.create<String> { emitter ->
            val listener = Emitter.Listener { args ->
                if (args == null) {
                    emitter.onNext("null");
                } else {
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

    private fun systemSocketEventFlowable(eventName: String, backpressureStrategy: BackpressureStrategy): Flowable<Unit> {
        checkSubscribedToEvent(eventName)
        return Flowable.create<Unit>({ emitter ->
            val listener = Emitter.Listener { _ ->
                /*if (args == null) {
                    emitter.onError(EmptySocketDataException(eventName))
                } else {*/
                    emitter.onNext(Unit)
               // }
            }
            socket.on(eventName, listener)
            socketEvents.add(eventName)
        }, backpressureStrategy)
                .doOnSubscribe {
                    compositeSubscription.add(it)
                }
    }

    private fun systemSocketEventErrorFlowable(eventName: String, backpressureStrategy: BackpressureStrategy): Flowable<String> {
        checkSubscribedToEvent(eventName)
        return Flowable.create<String>({ emitter ->
            val listener = Emitter.Listener { args ->
                if (args == null) {
                    emitter.onNext("null");
                } else {
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

    private fun checkSubscribedToEvent(event: String) {
        if (socketEvents.contains(event)) {
            throw EventAlreadySubscribedException(event)
        }
    }
}

fun createRxSocket(block: RxSocketBuilder.() -> Unit) = RxSocketBuilder().apply(block).build()


