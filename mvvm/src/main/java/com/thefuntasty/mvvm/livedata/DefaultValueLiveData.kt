package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Extended [LiveData] class with default value. Default value can't be null.
 */
@Deprecated("use UiData", ReplaceWith("com.thefuntasty.mvvm.livedata.uiData {defaultValue}" ))
class DefaultValueLiveData<T : Any>(defaultValue: T) : MutableLiveData<T>() {

    init {
        value = defaultValue
    }

    override fun getValue(): T = super.getValue() ?: throw NullPointerException("Value is null")
}
