package ua.andrii.chernysh.rxsockets.data.source

import io.reactivex.Observable

interface LightsDataSource: SocketDataSource{
    fun setLights(isOn: Boolean): Observable<Any>
}
