package ua.andrii.chernysh.kotlinrxsocket.exception

import java.lang.RuntimeException

class EventJsonSyntaxException(val eventName: String, jsonSyntaxExceptionMessage: String?):
        RuntimeException("Json syntax exception occurred in socket event $eventName." +
                " Message: ${jsonSyntaxExceptionMessage ?: "No message"}")