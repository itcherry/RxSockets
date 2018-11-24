package ua.diploma.kpi.kotlinrxsockets.socket

interface SocketLoggingInterceptor {
    fun logInfo(message: String)
    fun logError(message: String)
}