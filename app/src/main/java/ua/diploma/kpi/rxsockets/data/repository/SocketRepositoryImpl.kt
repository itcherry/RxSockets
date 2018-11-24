package ua.diploma.kpi.rxsockets.data.repository

import io.reactivex.Observable
import ua.diploma.kpi.kotlinrxsockets.socket.RxSocketEvent
import ua.diploma.kpi.rxsockets.data.source.SocketDataSource
import ua.diploma.kpi.rxsockets.domain.repository.SocketRepository
import javax.inject.Inject

abstract class SocketRepositoryImpl @Inject constructor(val socketDataSource: SocketDataSource): SocketRepository {
    override fun onSocketConnect() = socketDataSource.observableOnConnect()
    override fun onSocketDisconnect() = socketDataSource.observableOnDisconnect()
    override fun onSocketReconnect() = socketDataSource.observableOnReconnect()
    override fun onSocketReconnecting() = socketDataSource.observableOnReconnecting()
    override fun onSocketReconnectError() = socketDataSource.observableOnReconnectError()
    override fun onSocketError() = socketDataSource.observableOnError()
    override fun onSocketGenericEvent(): Observable<RxSocketEvent> = socketDataSource.observableOnGenericEvent()

    override fun connect() = socketDataSource.connect()
    override fun disconnect() = socketDataSource.disconnect()
}