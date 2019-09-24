package com.thefuntasty.mvvm.crinteractors

import kotlinx.coroutines.Deferred

abstract class BaseCoroutineInteractor<T> {
    var deferred: Deferred<T>? = null
    abstract suspend fun build(): T
}
