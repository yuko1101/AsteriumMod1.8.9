package io.github.yuko1101.asterium.events

import java.lang.reflect.Method

class EventListenerData(val source: Any, val target: Method, val priority: EventPriority)