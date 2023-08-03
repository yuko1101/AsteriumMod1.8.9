package io.github.yuko1101.asterium.utils

open class ObjectManager<T> {
    private val _items: ArrayList<T> = arrayListOf()

    val registered: List<T>
        get() = _items.toList()

    open fun register(item: T) {
        if (!_items.contains(item)) _items.add(item)
    }

    open fun unregister(item: T) {
        _items.remove(item)
    }
}