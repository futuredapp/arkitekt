package com.thefuntasty.mvvm

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import android.support.annotation.CallSuper
import com.thefuntasty.mvvm.event.Event
import com.thefuntasty.mvvm.event.LiveEventBus
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import kotlin.reflect.KClass

abstract class BaseViewModel : ViewModel(), Observable, LifecycleObserver {

    @Transient private var callbacks: PropertyChangeRegistry? = null

    private var onStartCalled = false
    private val liveEventBus = LiveEventBus()

    private val observers = mutableMapOf<Observer<Any>, LiveData<Any>>()

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

    @Suppress("UNCHECKED_CAST")
    fun <T> LiveData<T>.observeWithoutOwner(callback: (T?) -> Unit) {
        val observer = Observer<T> { callback(it) }
        observeForever(observer)
        observers += observer as Observer<Any> to this as LiveData<Any>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> DefaultValueLiveData<T>.observeWithoutOwner(callback: (T) -> Unit) {
        val observer = Observer<T> { it?.let(callback) }
        observeForever(observer)
        observers += observer as Observer<Any> to this as LiveData<Any>
    }

    private fun removeObservers() {
        observers.forEach { entry -> entry.value.removeObserver(entry.key) }
        observers.clear()
    }

    @CallSuper
    override fun onCleared() {
        removeObservers()
    }

    // ----- Observable implementation -----

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
