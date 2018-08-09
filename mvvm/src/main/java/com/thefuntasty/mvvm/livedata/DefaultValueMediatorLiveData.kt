package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class DefaultValueMediatorLiveData<T>(defaultValue: T) : MediatorLiveData<T>() {

    init {
        value = defaultValue
    }

    override fun getValue(): T {
        return super.getValue()!!
    }
}

fun <T> LiveData<T>.nonNull(defaultValue: T): DefaultValueMediatorLiveData<T> {
    val mediator: DefaultValueMediatorLiveData<T> = DefaultValueMediatorLiveData(defaultValue)
    mediator.addSource(this) { it?.let { mediator.value = it } }
    return mediator
}
