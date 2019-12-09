package com.thefuntasty.mvvm.crusecases

import kotlinx.coroutines.Deferred

/**
 * Base Coroutine use case meant to use in [CoroutineScopeOwner] implementations
 */
abstract class BaseUseCase<ARGS, T> {
    /**
     *  [Deferred] used to hold and cancel existing run of this use case
     */
    var deferred: Deferred<T>? = null

    /**
     * Suspend function which should contain business logic
     */
    abstract suspend fun build(args: ARGS): T
}
