package com.thefuntasty.mvvm.crinteractors

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

/**
 * Base [Flow] interactor meant to use in [CoroutineScopeOwner] implementations
 */
abstract class BaseFlowInteractor<ARGS, T> {

    /**
     *  [Job] used to hold and cancel existing run of this interactor
     */
    var job: Job? = null

    /**
     * Suspend function which should contain business logic
     */
    abstract suspend fun build(args: ARGS): Flow<T>
}
