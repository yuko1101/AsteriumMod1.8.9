package io.github.yuko1101.asterium.events

import net.minecraftforge.fml.common.eventhandler.Event
import java.lang.reflect.Method

class EventManager {
    private val _registered = mutableMapOf<Class<out Event>, ArrayList<EventListenerData>>()
    val registered: Map<Class<out Event>, List<EventListenerData>>
        get() = _registered.toMap().entries.associate { it.key to it.value.toList() }

    fun getListeners(event: Class<out Event>): List<EventListenerData> {
        return _registered.entries.filter { it.key.isAssignableFrom(event) }.map { it.value }.flatten()
    }

    private fun isValidMethod(method: Method): Boolean {
        return method.parameterTypes.size == 1 && method.isAnnotationPresent(EventTrigger::class.java)
    }

    private fun isValidMethod(method: Method, event: Class<out Event>): Boolean {
        return isValidMethod(method) && event.isAssignableFrom(method.parameterTypes[0])
    }

    fun register(method: Method, source: Any): EventListenerData {
        if (!isValidMethod(method, Event::class.java)) {
            throw Exception("Invalid method provided.")
        }
        @Suppress("UNCHECKED_CAST")
        val clazz: Class<out Event> = method.parameterTypes[0] as Class<out Event>
        val listenerData = EventListenerData(source, method, method.getAnnotation(EventTrigger::class.java).priority)

        if (!listenerData.target.isAccessible) {
            listenerData.target.isAccessible = true
        }

        if (_registered.containsKey(clazz)) {
            if (!_registered[clazz]!!.contains(listenerData)) {
                _registered[clazz]!!.add(listenerData)
                _registered[clazz]!!.sortWith(compareBy { it.priority })
            }
        } else {
            _registered[clazz] = arrayListOf(listenerData)
        }

        return listenerData
    }

    fun register(source: Any, event: Class<out Event>): List<EventListenerData> {
        val eventListenerDataList = arrayListOf<EventListenerData>()
        for (method: Method in source::class.java.methods) {
            if (!isValidMethod(method, event)) continue
            val eventListenerData = register(method, source)
            eventListenerDataList.add(eventListenerData)
        }
        return eventListenerDataList
    }

    fun register(source: Any): List<EventListenerData> {
        val eventListenerDataList = arrayListOf<EventListenerData>()
        for (method: Method in source::class.java.methods) {
            if (!isValidMethod(method)) continue
            val eventListenerData = register(method, source)
            eventListenerDataList.add(eventListenerData)
        }
        return eventListenerDataList
    }

    fun unregister(eventListenerData: EventListenerData) {
        _registered.values.forEach { it.remove(eventListenerData) }
    }

}