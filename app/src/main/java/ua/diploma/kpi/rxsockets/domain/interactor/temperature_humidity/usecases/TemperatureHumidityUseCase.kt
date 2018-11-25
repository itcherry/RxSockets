package ua.diploma.kpi.rxsockets.domain.interactor.temperature_humidity.usecases

import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import ua.diploma.kpi.rxsockets.data.source.DataPolicy
import ua.diploma.kpi.rxsockets.domain.ObservableUseCase
import ua.diploma.kpi.rxsockets.domain.model.TemperatureHumidityData
import ua.diploma.kpi.rxsockets.domain.repository.TemperatureHumidityRepository
import javax.inject.Inject

class TemperatureHumidityUseCase @Inject constructor(observableTransformer: ObservableTransformer<Any, Any>,
                                                     compositeDisposable: CompositeDisposable ,
                                                     private val temperatureHumidityRepository: TemperatureHumidityRepository):
        ObservableUseCase<TemperatureHumidityData, Unit>(observableTransformer, compositeDisposable){

    override fun buildUseCaseObservable(params: Unit) = temperatureHumidityRepository.temperatureHumidityObservable(DataPolicy.SOCKET)
}