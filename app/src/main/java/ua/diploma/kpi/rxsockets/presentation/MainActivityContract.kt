package ua.diploma.kpi.rxsockets.presentation

import io.reactivex.Observable
import ua.diploma.kpi.rxsockets.domain.model.TemperatureHumidityData

interface MainActivityContract {

    interface View {
        fun showTemperatureHumidity(temperatureHumidityData: TemperatureHumidityData)
        fun showSocketConnected()
        fun showSocketDisconnected()
        fun showSocketConnecting()
        fun showSocketError()
        fun showSocketReconnecting()
    }

    interface Presenter {
        fun initSockets()
        fun enableLightObservable(clickObservable: Observable<Any>)
        fun disableLightObservable(clickObservable: Observable<Any>)
    }
}