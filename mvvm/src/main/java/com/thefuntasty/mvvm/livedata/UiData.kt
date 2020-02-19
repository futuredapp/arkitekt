package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.MutableLiveData

/**
 *  Mutable live data abstraction with non nullable value type.
 *  You need to initialize it with non null value. You can use
 *  one of the init functions or extension function
 *  @see [uiData("data")] [uiData]
 *  @see [uiData { "data" }] [uiData]
 *  @see ["data".toUiData()] [toUiData]
 *
 *  @param T non null value type
 *  @property initValue first value emitted by UiData
 *
 *  map function returns NonNullLiveData<R>
 *  @see NonNullLiveData
 */
class UiData<T : Any>(initValue: T) : MutableLiveData<T>() {
    init {
        value = initValue
    }

    override fun getValue(): T = super.getValue() ?: throw NullPointerException("Value is null")

    fun <R : Any> map(mapper: (T) -> R): NonNullLiveData<R> {
        val nonNullLiveData = NonNullLiveData(mapper(this.value))
        val mediator = UiDataMediator(mapper(this.value))
        mediator.addSource(this) {
            it?.let {
                nonNullLiveData.value = mapper(it)
            }
        }
        return nonNullLiveData
    }
}
