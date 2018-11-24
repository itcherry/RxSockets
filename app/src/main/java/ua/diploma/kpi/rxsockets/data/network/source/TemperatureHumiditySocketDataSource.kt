package ua.diploma.kpi.rxsockets.data.network.source

import io.reactivex.Observable
import ua.diploma.kpi.kotlinrxsockets.socket.RxSocket
import ua.diploma.kpi.rxsockets.data.model.TemperatureHumidityDTO
import ua.diploma.kpi.rxsockets.data.source.TemperatureHumidityDataSource
import javax.inject.Inject

/**
 * Copyright 2017. Andrii Chernysh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
class TemperatureHumiditySocketDataSource @Inject constructor(val socket: RxSocket) :
        SocketDataSource(socket), TemperatureHumidityDataSource {

    override fun getTemperature(): Observable<TemperatureHumidityDTO> =
        socket.observableOn(TEMP_HUM_EVENT, TemperatureHumidityDTO::class.java)

    companion object {
        const val TEMP_HUM_EVENT = "tempHum"
    }
}