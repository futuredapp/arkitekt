package com.thefuntasty.mvvm.crinteractors

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

/**
 * Base [Flow] interactor meant to use in [CoroutineScopeOwner] implementations
 */
abstract class BaseFlowInteractor<T> {

    /**
     *  [Job] used to hold and cancel existing run of this interactor
     */
    var job: Job? = null

    /**
     * Suspend function which should contain business logic
     */
    abstract suspend fun build(): Flow<T>
}
