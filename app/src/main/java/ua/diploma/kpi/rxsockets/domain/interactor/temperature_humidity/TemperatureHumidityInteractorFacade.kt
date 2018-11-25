package ua.diploma.kpi.rxsockets.domain.interactor.temperature_humidity

import ua.diploma.kpi.kotlinrxsockets.socket.RxSocketEvent
import ua.diploma.kpi.rxsockets.domain.interactor.temperature_humidity.usecases.SocketGenericEventUseCase
import ua.diploma.kpi.rxsockets.domain.interactor.temperature_humidity.usecases.TemperatureHumidityUseCase
import ua.diploma.kpi.rxsockets.domain.model.TemperatureHumidityData
import ua.diploma.kpi.rxsockets.domain.repository.TemperatureHumidityRepository
import javax.inject.Inject

class TemperatureHumidityInteractorFacade @Inject constructor(
        private val socketGenericUseCase: SocketGenericEventUseCase,
        private val temperatureHumidityUseCase: TemperatureHumidityUseCase,
        private val temperatureHumidityRepository: TemperatureHumidityRepository){
    fun onSocketEventListener(listener: (rxSocketEvent: RxSocketEvent)-> Unit){
        socketGenericUseCase.execute(Unit, listener)
    }

    fun onTemperatureHumidityListener(listener: (data: TemperatureHumidityData)-> Unit){
        temperatureHumidityUseCase.execute(Unit, listener)
    }

    fun connect(){
        temperatureHumidityRepository.connect()
    }

    fun disconnect(){
        temperatureHumidityRepository.disconnect()
        socketGenericUseCase.dispose()
        temperatureHumidityUseCase.dispose()
    }
}