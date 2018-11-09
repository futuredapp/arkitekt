package com.thefuntasty.mvvm.event

import androidx.collection.ArrayMap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.thefuntasty.mvvm.ViewState
import kotlin.reflect.KClass

class LiveEventBus<T : ViewState> {

    private val eventMap: ArrayMap<KClass<out Event<T>>, LiveEvent<out Event<T>>> = ArrayMap()

    fun observe(lifecycleOwner: LifecycleOwner, eventClass: KClass<out Event<T>>, observer: (Event<T>) -> Unit) {
        @Suppress("UNCHECKED_CAST")
        var liveEvent: LiveEvent<Event<T>>? = eventMap[eventClass] as LiveEvent<Event<T>>?
        if (liveEvent == null) {
            liveEvent = initLiveEvent(eventClass)
        }
        liveEvent.observe(lifecycleOwner, Observer { observer.invoke(it) })
    }

    fun send(event: Event<T>) {
        var liveEvent: LiveEvent<*>? = eventMap[event::class]
        if (liveEvent == null) {
            liveEvent = initLiveEvent(event::class)
        }
        liveEvent.value = event
    }

    private fun initLiveEvent(eventClass: KClass<out Event<T>>): LiveEvent<Event<T>> {
        val liveEvent = LiveEvent<Event<T>>()
        eventMap[eventClass] = liveEvent
        return liveEvent
    }
}
