package com.thefuntasty.mvvm

import androidx.annotation.CallSuper
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.thefuntasty.mvvm.event.Event
import com.thefuntasty.mvvm.event.LiveEventBus
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import com.thefuntasty.mvvm.livedata.DefaultValueMediatorLiveData
import kotlin.reflect.KClass

/**
 * Base class representing ViewModel. It allows to observe [LiveData]s what is useful
 * for observing ViewState. Observers are automatically removed when ViewModel is
 * no longer used and will be destroyed. Beside that it handles one-shot [Event]s
 * send from ViewModel to Activity/Fragment.
 */
abstract class BaseViewModel<VS : ViewState> : ViewModel(), Observable, LifecycleObserver {

    abstract val viewState: VS

    @Transient private var callbacks: PropertyChangeRegistry? = null

    private var onStartCalled = false
    private val liveEventBus = LiveEventBus<VS>()

    private val observers = mutableMapOf<Observer<Any>, LiveData<Any>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    internal fun onLifeCycleStart() {
        if (!onStartCalled) {
            onStart()
            onStartCalled = true
        }
    }

    /**
     * Activity's onStart() method companion. Method is executed only once.
     */
    open fun onStart() {}

    /**
     * Send one-shot event to internal bus and notify all of its observers
     * @param event event to be send
     */
    fun sendEvent(event: Event<VS>) {
        liveEventBus.send(event)
    }

    /**
     * Observe [LiveData] without particular owner (Activity eg.). Observer is removed
     * when ViewModel is destroyed.
     * @param callback Lambda called when new value is dispatched to [LiveData]
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> LiveData<T>.observeWithoutOwner(callback: (T) -> Unit) {
        val observer = Observer<T> { callback(it) }
        observeForever(observer)
        observers += observer as Observer<Any> to this as LiveData<Any>
    }

    /**
     * Observe [DefaultValueLiveData] without particular owner (Activity eg.).
     * Observer is removed when ViewModel is destroyed.
     * @param callback Lambda called when new value is dispatched to [LiveData]
     */
    fun <T : Any> DefaultValueLiveData<T>.observeWithoutOwner(callback: (T) -> Unit) {
        observeLiveDataNonNull(this, callback)
    }

    /**
     * Observe [DefaultValueMediatorLiveData] without particular owner (Activity eg.).
     * Observer is removed when ViewModel is destroyed.
     * @param callback Lambda called when new value is dispatched to [LiveData]
     */
    fun <T : Any> DefaultValueMediatorLiveData<T>.observeWithoutOwner(callback: (T) -> Unit) {
        observeLiveDataNonNull(this, callback)
    }

    internal fun observeEvent(
        lifecycleOwner: LifecycleOwner,
        eventClass: KClass<out Event<VS>>,
        observer: (Event<VS>) -> Unit
    ) {
        liveEventBus.observe(lifecycleOwner, eventClass, observer)
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
     * that changes should be marked with [@Bindable] to generate a field in
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
