package com.thefuntasty.mvvm.crinteractors

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

abstract class BaseFlowInteractor<T> {
    var job: Job? = null
    abstract suspend fun build(): Flow<T>
}
