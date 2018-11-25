package ua.diploma.kpi.rxsockets.data.source

import io.reactivex.Observable

interface LightsDataSource: SocketDataSource{
    fun setLights(isOn: Boolean): Observable<Any>
}
