package ua.andrii.chernysh.kotlinrxsocket.socket

import com.google.gson.Gson
import io.socket.client.IO

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
 * Builder for RxSocket for creating pretty DSL
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 */
class RxSocketBuilder{
    var hostIp: String = "PLEASE_SET_HOST_IP_AND_PORT"
    var port: Int = 0
    var namespace: String = ""
    var options: IO.Options? = null
    var gson: Gson = Gson()
    var socketLoggingInterceptor: SocketLoggingInterceptor? = null

    fun options(block: OptionsBuilder.() -> Unit) {
        options = OptionsBuilder().apply(block).build()
    }

    fun build() = RxSocket(hostIp, port, namespace, options, gson, socketLoggingInterceptor)
}