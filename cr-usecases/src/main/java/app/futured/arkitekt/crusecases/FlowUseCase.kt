package app.futured.arkitekt.crusecases

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

/**
 * Base [Flow] use case meant to use in [CoroutineScopeOwner] implementations
 */
abstract class FlowUseCase<ARGS, T> {

    /**
     *  [Job] used to hold and cancel existing run of this use case
     */
    var job: Job? = null

    /**
     * Function which builds Flow instance based on given arguments
     * @param args initial use case arguments
     */
    abstract fun build(args: ARGS): Flow<T>
}
