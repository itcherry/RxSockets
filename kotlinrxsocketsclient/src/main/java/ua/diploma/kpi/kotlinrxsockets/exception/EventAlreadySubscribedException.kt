package ua.diploma.kpi.kotlinrxsockets.exception

import java.lang.RuntimeException

class EventAlreadySubscribedException(eventName: String):
        RuntimeException("Socket already subscribed to the event $eventName. Don\'t duplicate subscriptions")