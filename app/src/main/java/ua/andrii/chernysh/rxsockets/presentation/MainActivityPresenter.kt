package ua.andrii.chernysh.rxsockets.presentation

import io.reactivex.Observable
import ua.andrii.chernysh.kotlinrxsocket.socket.RxSocketEvent
import ua.andrii.chernysh.rxsockets.domain.interactor.lights.LightsUseCase
import ua.andrii.chernysh.rxsockets.domain.interactor.temperature_humidity.TemperatureHumidityInteractorFacade
import ua.andrii.chernysh.rxsockets.domain.model.TemperatureHumidityData
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(private val temperatureHumidityInteractorFacade: TemperatureHumidityInteractorFacade,
                                                private val lightsUseCase: LightsUseCase) :
        BasePresenter<TemperatureHumidityData, MainActivityContract.View>(), MainActivityContract.Presenter {
    override fun updateView() {
        view()?.showTemperatureHumidity(model ?: TemperatureHumidityData(0.0, 0.0))
    }

    override fun destroy() {
        temperatureHumidityInteractorFacade.disconnect()
    }

    override fun initSockets() {
        temperatureHumidityInteractorFacade.onSocketEventListener {
            when (it) {
                RxSocketEvent.CONNECTED -> view()?.showSocketConnected()
                RxSocketEvent.CONNECTING -> view()?.showSocketConnecting()
                RxSocketEvent.DISCONNECTED -> view()?.showSocketDisconnected()
                RxSocketEvent.RECONNECTING -> view()?.showSocketReconnecting()
                RxSocketEvent.ERROR, RxSocketEvent.RECONNECT_ERROR, RxSocketEvent.CONNECT_ERROR -> view()?.showSocketError()
                else -> {
                }
            }
        }

        temperatureHumidityInteractorFacade.onTemperatureHumidityListener {
            model = it
            view()?.showTemperatureHumidity(it)
        }

        temperatureHumidityInteractorFacade.connect()
    }

    override fun enableLightObservable(clickObservable: Observable<Any>) {
        addDisposable(clickObservable
                .subscribe {
                    lightsUseCase.execute(true)
                })
    }

    override fun disableLightObservable(clickObservable: Observable<Any>) {
        addDisposable(clickObservable
                .subscribe {
                    lightsUseCase.execute(false)
                })
    }
}
