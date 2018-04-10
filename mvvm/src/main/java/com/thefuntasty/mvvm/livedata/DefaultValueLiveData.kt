package com.thefuntasty.mvvm.livedata

import android.arch.lifecycle.MutableLiveData

class DefaultValueLiveData<T>(defaultValue: T) : MutableLiveData<T>() {

    init {
        value = defaultValue
    }

    fun value(): T = value!!
}
