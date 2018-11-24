package ua.diploma.kpi.kotlinrxsockets.socket

enum class RxSocketEvent {
    CONNECTED,
    CONNECTING,
    CONNECT_ERROR,
    CONNECT_TIMEOUT,
    DISCONNECTED,
    ERROR,
    MESSAGE,
    PING,
    PONG,
    RECONNECTED,
    RECONNECTING,
    RECONNECT_ATTEMPT,
    RECONNECT_ERROR,
    RECONNECT_FAILED,
    SEND_DATA_ERROR
}