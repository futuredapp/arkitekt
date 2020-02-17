package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, callback: (T?) -> Unit) {
    observe(lifecycleOwner, Observer { callback(it) })
}

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, callback: (T) -> Unit) {
    observe(lifecycleOwner, Observer { it?.let(callback) })
}

fun <T : Any> LiveData<T?>.toNonNull(initValue: T): NonNullLiveData<T> {
    val mediator  = UiDataMediator(initValue)
    return NonNullLiveData(initValue, mediator, this) // todo NonNullMediator.addSource(this)
}
