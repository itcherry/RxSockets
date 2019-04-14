package ua.andrii.chernysh.rxsockets.domain.repository

import io.reactivex.Observable
import ua.andrii.chernysh.rxsockets.data.source.DataPolicy
import ua.andrii.chernysh.rxsockets.domain.model.TemperatureHumidityData

interface TemperatureHumidityRepository: SocketRepository {
    fun temperatureHumidityObservable(dataPolicy: DataPolicy): Observable<TemperatureHumidityData>
}