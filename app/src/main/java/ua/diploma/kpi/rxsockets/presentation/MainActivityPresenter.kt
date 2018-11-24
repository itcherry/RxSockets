package ua.diploma.kpi.rxsockets.presentation

import io.reactivex.Observable
import ua.diploma.kpi.rxsockets.domain.interactor.TemperatureHumidityInteractorFacade
import ua.diploma.kpi.rxsockets.domain.model.TemperatureHumidityData
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(private val temperatureHumidityInteractorFacade: TemperatureHumidityInteractorFacade):
        BasePresenter<TemperatureHumidityData, MainActivityContract.View>(), MainActivityContract.Presenter {
    override fun updateView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initSockets() {
        temperatureHumidityInteractorFacade.onSocketEventListener {
            when(it){

            }
        }
    }

    override fun enableLightObservable(clickObservable: Observable<Any>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disableLightObservable(clickObservable: Observable<Any>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
