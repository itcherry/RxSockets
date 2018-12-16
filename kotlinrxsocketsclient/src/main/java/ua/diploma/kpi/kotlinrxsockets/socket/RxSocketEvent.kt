package ua.diploma.kpi.kotlinrxsockets.socket

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
 * Enum that contains all possible system socket events
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 */
enum class RxSocketEvent {
    CONNECTED,
    CONNECTING,
    CONNECT_ERROR,
    CONNECT_TIMEOUT,
    DISCONNECTED,
    ERROR,
    MESSAGE,
    PING,
    PONG,
    RECONNECTED,
    RECONNECTING,
    RECONNECT_ATTEMPT,
    RECONNECT_ERROR,
    RECONNECT_FAILED,
    SEND_DATA_ERROR
}