package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.LiveData

class NonNullLiveData<T : Any>(
    initValue: T,
    private val mediator: UiDataMediator<T>? = null,
    private val source: LiveData<T?>? = null
) : LiveData<T>() {

    init {
        value = initValue
    }

    override fun onActive() {
        super.onActive()
        mediator?.let { uiMediator ->
            source?.let {source ->
                uiMediator.addSource(source) {
                    it?.let {
                        value = it
                    }
                }
            }
        }
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
