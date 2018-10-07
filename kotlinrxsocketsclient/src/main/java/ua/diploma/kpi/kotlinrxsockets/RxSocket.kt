package ua.diploma.kpi.kotlinrxsockets

import io.socket.client.IO
import io.socket.client.Socket

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
 *         Developed by <u>Transcendensoft</u>
 */
class RxSocket(hostIp: String, port: Int, namespace: String, options: IO.Options? = null) {
    private val socket: Socket

    init {
        options?.reconnection
        socket = if (options == null) {
            IO.socket("$hostIp:$port/$namespace")
        } else {
            IO.socket("$hostIp:$port/$namespace", options)
        }

    }

   // infix fun listenTo(eventName: String) = eventName.event

    infix fun String.event(x: event) = SocketEvent(this)
}

object event

/*class EventWrapper(val eventName: String){
    infix fun event()
}*/
fun createRxSocket(block: RxSocketBuilder.() -> Unit) = RxSocketBuilder().apply(block).build()


