package ua.andrii.chernysh.rxsockets.domain.interactor.temperature_humidity

import ua.andrii.chernysh.kotlinrxsocket.socket.RxSocketEvent
import ua.andrii.chernysh.rxsockets.domain.interactor.temperature_humidity.usecases.SocketGenericEventUseCase
import ua.andrii.chernysh.rxsockets.domain.interactor.temperature_humidity.usecases.TemperatureHumidityUseCase
import ua.andrii.chernysh.rxsockets.domain.model.TemperatureHumidityData
import ua.andrii.chernysh.rxsockets.domain.repository.TemperatureHumidityRepository
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