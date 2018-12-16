package ua.andrii.chernysh.kotlinrxsocket.socket

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
 * Builder for IO.Options for creating pretty DSL
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 */
class OptionsBuilder {
    var forceNew: Boolean = false

    var query: String = ""

    var reconnection: Boolean = true
    var reconnectionAttempts: Int = 0
    var reconnectionDelay: Long = 1000L
    var reconnectionDelayMax: Long = 5000L
    var timeout: Long = 20000L
    var randomizationFactor = 0.5

    fun build(): IO.Options {
        val options = IO.Options()
        options.forceNew = forceNew
        options.query = query
        options.reconnection = reconnection
        options.reconnectionAttempts = reconnectionAttempts
        options.reconnectionDelay = reconnectionDelay
        options.reconnectionDelayMax = reconnectionDelayMax
        options.timeout = timeout
        options.randomizationFactor = randomizationFactor

        return options
    }
}