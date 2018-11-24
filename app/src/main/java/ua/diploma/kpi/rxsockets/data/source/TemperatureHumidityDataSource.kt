package ua.diploma.kpi.rxsockets.data.source

import io.reactivex.Observable
import ua.diploma.kpi.rxsockets.data.model.TemperatureHumidityDTO

/**
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 */
interface TemperatureHumidityDataSource: SocketDataSource{
    fun getTemperature(): Observable<TemperatureHumidityDTO>
}
