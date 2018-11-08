package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.MutableLiveData

class DefaultValueLiveData<T>(defaultValue: T) : MutableLiveData<T>() {

    init {
        value = defaultValue
    }

    override fun getValue(): T {
        return super.getValue()!!
    }
}
