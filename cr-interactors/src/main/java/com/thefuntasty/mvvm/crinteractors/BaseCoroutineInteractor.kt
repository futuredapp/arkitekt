package com.thefuntasty.mvvm.crinteractors

import kotlinx.coroutines.Deferred

/**
 * Base interactor meant to use in [CoroutineScopeOwner] implementations
 */
abstract class BaseCoroutineInteractor<ARGS, T> {
    /**
     *  [Deferred] used to hold and cancel existing run of this interactor
     */
    var deferred: Deferred<T>? = null

    /**
     * Suspend function which should contain business logic
     */
    abstract suspend fun build(args: ARGS): T
}
