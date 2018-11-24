package ua.diploma.kpi.rxsockets.domain.repository

import io.reactivex.Observable
import ua.diploma.kpi.kotlinrxsockets.socket.RxSocketEvent

interface SocketRepository{
    fun onSocketConnect(): Observable<Unit>
    fun onSocketDisconnect(): Observable<Unit>
    fun onSocketReconnect() : Observable<Unit>
    fun onSocketReconnecting() : Observable<Unit>
    fun onSocketReconnectError() : Observable<String>
    fun onSocketError(): Observable<String>
    fun onSocketGenericEvent(): Observable<RxSocketEvent>

    fun connect()
    fun disconnect()
}