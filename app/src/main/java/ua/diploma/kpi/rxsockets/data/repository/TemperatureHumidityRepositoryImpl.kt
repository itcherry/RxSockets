package ua.diploma.kpi.rxsockets.data.repository

import io.reactivex.Observable
import ua.diploma.kpi.rxsockets.data.exception.DataPolicyNotImplemented
import ua.diploma.kpi.rxsockets.data.source.DataPolicy
import ua.diploma.kpi.rxsockets.data.source.TemperatureHumidityDataSource
import ua.diploma.kpi.rxsockets.domain.model.TemperatureHumidityData
import ua.diploma.kpi.rxsockets.domain.repository.TemperatureHumidityRepository
import javax.inject.Inject

class TemperatureHumidityRepositoryImpl @Inject constructor(
        private val temperatureHumidityDataSource: TemperatureHumidityDataSource
) : SocketRepositoryImpl(temperatureHumidityDataSource), TemperatureHumidityRepository {

    override fun temperatureHumidityObservable(dataPolicy: DataPolicy): Observable<TemperatureHumidityData> {
        return when(dataPolicy){
            DataPolicy.SOCKET -> temperatureHumidityDataSource.getTemperature().map { it.mapToEntity() }
            else -> Observable.error(DataPolicyNotImplemented(dataPolicy))
        }
    }
}