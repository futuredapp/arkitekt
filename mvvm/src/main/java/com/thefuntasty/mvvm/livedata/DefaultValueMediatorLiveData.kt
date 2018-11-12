package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import java.lang.NullPointerException

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
