package app.futured.arkitekt.core.livedata

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
 *  map function returns UiDataMediator<R>
 *  @see UiDataMediator
 */
class UiData<T : Any>(initValue: T) : MutableLiveData<T>() {
    init {
        value = initValue
    }

    override fun getValue(): T = super.getValue() ?: throw NullPointerException("Value is null")

    /**
     * Inform user about non-nullability from Java call site
     * */
    @Suppress("RedundantOverride")
    override fun setValue(value: T) {
        super.setValue(value)
    }

    /**
     * Inform user about non-nullability from Java call site
     * */
    @Suppress("RedundantOverride")
    override fun postValue(value: T) {
        super.postValue(value)
    }

    fun <R : Any> map(mapper: (T) -> R): UiDataMediator<R> {
        val mediator = UiDataMediator(mapper(this.value))
        mediator.addSource(this) {
            it?.let {
                mediator.value = mapper(it)
            }
        }
        return mediator
    }
}
