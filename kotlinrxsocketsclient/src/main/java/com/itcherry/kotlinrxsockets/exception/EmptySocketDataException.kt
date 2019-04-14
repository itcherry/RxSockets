package com.itcherry.kotlinrxsockets.exception

import java.lang.RuntimeException

class EmptySocketDataException(eventName: String) :
        RuntimeException("You received null from socket event $eventName")