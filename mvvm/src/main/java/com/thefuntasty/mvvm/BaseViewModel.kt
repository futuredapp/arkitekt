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
import android.os.SystemClock
import com.thefuntasty.mvvm.event.Event
import com.thefuntasty.mvvm.event.LiveEventBus
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import com.thefuntasty.mvvm.livedata.DefaultValueMediatorLiveData
import kotlin.reflect.KClass

abstract class BaseViewModel<E : ViewState> : ViewModel(), Observable, LifecycleObserver {

    abstract val viewState: E

    @Transient private var callbacks: PropertyChangeRegistry? = null

    private var onStartCalled = false
    private val liveEventBus = LiveEventBus<E>()

    private val observers = mutableMapOf<Observer<Any>, LiveData<Any>>()

    private val lastClickMap = mutableListOf<Long>()

    open val clickDebounce = 1000L

    open fun onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onLifeCycleStart() {
        if (!onStartCalled) {
            onStart()
            onStartCalled = true
        }
    }

    fun observeEvent(lifecycleOwner: LifecycleOwner, eventClass: KClass<out Event<E>>, observer: (Event<E>) -> Unit) {
        liveEventBus.observe(lifecycleOwner, eventClass, observer)
    }

    fun sendEvent(event: Event<E>) {
        liveEventBus.send(event)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> LiveData<T>.observeWithoutOwner(callback: (T?) -> Unit) {
        val observer = Observer<T> { callback(it) }
        observeForever(observer)
        observers += observer as Observer<Any> to this as LiveData<Any>
    }

    fun <T> DefaultValueLiveData<T>.observeWithoutOwner(callback: (T) -> Unit) {
        observeLiveDataNonNull(this, callback)
    }

    fun <T> DefaultValueMediatorLiveData<T>.observeWithoutOwner(callback: (T) -> Unit) {
        observeLiveDataNonNull(this, callback)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> observeLiveDataNonNull(liveData: LiveData<T>, callback: (T) -> Unit) {
        val observer = Observer<T> { it?.let(callback) }
        liveData.observeForever(observer)
        observers += observer as Observer<Any> to liveData as LiveData<Any>
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

    /**
     * Simultaneous click protection. Ignores multi clicks, missclicks and other unwanted
     * behaviour by ignoring code block in onDebounce DSL block for time period
     * specified by clickDebounce param
     * clickDebbounce default value is set to 1000L which represents 1 second delay.
     * clickDebounce param is overridable
     *
     * @param block executes only when there is delay larger than clickDebounce
     */
    fun onDebounce(block: () -> Unit) {
        val previousClickTimestamp = lastClickMap.lastOrNull()
        val currentTimestamp = SystemClock.uptimeMillis()

        lastClickMap += currentTimestamp
        if (previousClickTimestamp == null ||
                currentTimestamp - previousClickTimestamp.toLong() > clickDebounce) {
            block.invoke()
        }
        if (lastClickMap.size > 4) {
            lastClickMap.removeAt(0)
            lastClickMap.removeAt(1)
        }
    }
}
