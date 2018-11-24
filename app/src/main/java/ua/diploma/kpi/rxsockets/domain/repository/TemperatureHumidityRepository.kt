package ua.diploma.kpi.rxsockets.domain.repository

import io.reactivex.Observable
import ua.diploma.kpi.rxsockets.data.source.DataPolicy
import ua.diploma.kpi.rxsockets.domain.model.TemperatureHumidityData

interface TemperatureHumidityRepository: SocketRepository {
    fun temperatureHumidityObservable(dataPolicy: DataPolicy): Observable<TemperatureHumidityData>
}