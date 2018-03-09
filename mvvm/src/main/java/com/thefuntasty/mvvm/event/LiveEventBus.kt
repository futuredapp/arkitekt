package com.thefuntasty.mvvm.event

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v4.util.ArrayMap
import kotlin.reflect.KClass

class LiveEventBus {

    private val eventMap: ArrayMap<KClass<out Event>, LiveEvent<out Event>> = ArrayMap()

    fun <T : Event> observe(lifecycleOwner: LifecycleOwner, eventClass: KClass<T>, observer: (T) -> Unit) {
        var liveEvent: LiveEvent<T>? = eventMap[eventClass] as LiveEvent<T>?
        if (liveEvent == null) {
            liveEvent = initLiveEvent(eventClass)
        }
        liveEvent.observe(lifecycleOwner, Observer { observer.invoke(it!!) })
    }

    fun <T : Event> send(event: T) {
        var liveEvent: LiveEvent<*>? = eventMap[event::class]
        if (liveEvent == null) {
            liveEvent = initLiveEvent(event::class)
        }
        liveEvent.value = event
    }

    private fun <T : Event> initLiveEvent(eventClass: KClass<T>): LiveEvent<T> {
        val liveEvent = LiveEvent<T>()
        eventMap[eventClass] = liveEvent
        return liveEvent
    }
}
