package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.LiveData

/**
 * Immutable live data abstraction capable of mapping
 * and preserving the non null value
 */
class NonNullLiveData<T : Any>(initValue: T) : LiveData<T>() {

    init {
        value = initValue
    }

    override fun getValue(): T = super.getValue() ?: throw NullPointerException("Value is null")

    fun <B : Any> map(mapper: (T) -> B): NonNullLiveData<B> {
        val nonNullLiveData = NonNullLiveData(mapper(this.value))
        val mediatorLiveData = UiDataMediator(mapper(this.value))
        mediatorLiveData.addSource(this) {
            it?.let {
                nonNullLiveData.value = mapper(it)
            }
        }
        return nonNullLiveData
    }
}
