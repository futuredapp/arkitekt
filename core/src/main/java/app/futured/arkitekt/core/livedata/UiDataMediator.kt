package app.futured.arkitekt.core.livedata

import androidx.lifecycle.MediatorLiveData

class UiDataMediator<T : Any>(initValue: T) : MediatorLiveData<T>() {
    init {
        value = initValue
    }

    override fun getValue(): T = super.getValue() ?: throw NullPointerException("Value is null")
}
