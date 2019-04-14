package ua.andrii.chernysh.rxsockets.data.network.source

import com.google.gson.Gson
import io.reactivex.Observable
import ua.andrii.chernysh.kotlinrxsocket.socket.RxSocket
import ua.andrii.chernysh.rxsockets.data.model.LightsDTO
import ua.andrii.chernysh.rxsockets.data.source.LightsDataSource
import javax.inject.Inject

class LightsSocketDataSource @Inject constructor(val socket: RxSocket, val gson: Gson) :
        SocketDataSource(socket), LightsDataSource {

    override fun setLights(isOn: Boolean): Observable<Any> =
            Observable.fromCallable { socket.sendData(LIGHTS_EVENT, LightsDTO(isOn)) }

    companion object {
        const val LIGHTS_EVENT = "light"
    }
}