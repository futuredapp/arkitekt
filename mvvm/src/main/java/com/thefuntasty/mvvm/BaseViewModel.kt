package com.thefuntasty.mvvm

import android.arch.lifecycle.*
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.thefuntasty.mvvm.event.Event
import com.thefuntasty.mvvm.event.LiveEventBus
import kotlin.reflect.KClass

abstract class BaseViewModel : ViewModel(), Observable, LifecycleObserver {

    @Transient private var callbacks: PropertyChangeRegistry? = null

    private var onStartCalled = false
    private val liveEventBus = LiveEventBus()

    open fun onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onLifeCycleStart() {
        if (!onStartCalled) {
            onStart()
            onStartCalled = true
        }
    }

    fun <T : Event> observeEvent(lifecycleOwner: LifecycleOwner, eventClass: KClass<T>, observer: (T) -> Unit) {
        liveEventBus.observe(lifecycleOwner, eventClass, observer)
    }

    fun <T : Event> sendEvent(event: T) {
        liveEventBus.send(event)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (callbacks == null) {
                callbacks = PropertyChangeRegistry()
            }
        }
        callbacks?.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (callbacks == null) {
                return
            }
        }
        callbacks?.remove(callback)
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    fun notifyChange() {
        synchronized(this) {
            if (callbacks == null) {
                return
            }
        }
        callbacks?.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        synchronized(this) {
            if (callbacks == null) {
                return
            }
        }
        callbacks?.notifyCallbacks(this, fieldId, null)
    }
}
