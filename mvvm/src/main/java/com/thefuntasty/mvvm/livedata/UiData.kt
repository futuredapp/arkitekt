package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map

class UiData<T : Any>(initValue: T) : MutableLiveData<T>() {
    init {
        value = initValue
    }

    override fun getValue(): T = super.getValue() ?: throw NullPointerException("Value is null")

    fun <B : Any> map(mapper: (T) -> B): NonNullLiveData<B> {
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

val uiData = "bball".toUiData()
val ui = uiData(23)
val lbj = uiData { "LeBron James" }
val def = DefaultValueLiveData("game")

fun test() {
    uiData.value = "purple"
    val immutData = uiData.map { it.length }.map { 2 }
    immutData.value = 23
    val nonNull: Int = immutData.value
}

fun test2() {
    def.value = "gold"
    val immutDef = def.map { it.length }
    immutDef.value = 123
    val nonNull: Int = immutDef.value

}


