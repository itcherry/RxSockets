package ua.diploma.kpi.rxsockets.data.network.source

import io.reactivex.Observable
import ua.diploma.kpi.kotlinrxsockets.socket.RxSocket
import ua.diploma.kpi.rxsockets.data.source.LightsDataSource
import javax.inject.Inject

class LightsSocketDataSource @Inject constructor(val socket: RxSocket) :
        SocketDataSource(socket), LightsDataSource {

    override fun setLights(isOn: Boolean): Observable<Any> =
            Observable.fromCallable { socket.sendData(LIGHTS_EVENT, isOn) }

    companion object {
        const val LIGHTS_EVENT = "light"
    }
}