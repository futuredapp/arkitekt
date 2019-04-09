package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Extended [MediatorLiveData] class with default value. Default value can't be null.
 */
class DefaultValueMediatorLiveData<T : Any>(defaultValue: T) : MediatorLiveData<T>() {

    init {
        value = defaultValue
    }

    override fun getValue(): T = super.getValue() ?: throw NullPointerException("Value is null")
}

fun <T : Any> LiveData<T>.nonNull(defaultValue: T): DefaultValueMediatorLiveData<T> {
    val mediator: DefaultValueMediatorLiveData<T> = DefaultValueMediatorLiveData(defaultValue)
    mediator.addSource(this) { it?.let { mediator.value = it } }
    return mediator
}
