package ua.andrii.chernysh.rxsockets.domain.interactor.temperature_humidity.usecases

import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import ua.andrii.chernysh.kotlinrxsocket.socket.RxSocketEvent
import ua.andrii.chernysh.rxsockets.domain.ObservableUseCase
import ua.andrii.chernysh.rxsockets.domain.repository.TemperatureHumidityRepository
import javax.inject.Inject

class SocketGenericEventUseCase @Inject constructor(observableTransformer: ObservableTransformer<Any, Any>,
                                                    compositeDisposable: CompositeDisposable,
                                                    private val temperatureHumidityRepository: TemperatureHumidityRepository):
        ObservableUseCase<RxSocketEvent, Unit>(observableTransformer, compositeDisposable){

    override fun buildUseCaseObservable(params: Unit) = temperatureHumidityRepository.onSocketGenericEvent()
}