package ua.andrii.chernysh.rxsockets.domain.repository

import io.reactivex.Observable

interface LightsRepository: SocketRepository {
    fun setLight(isOn: Boolean): Observable<Any>
}