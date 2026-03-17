package app.futured.arkitekt.crusecases

import kotlinx.coroutines.flow.Flow

/**
 * Base [Flow] use case meant to use in [CoroutineScopeOwner] implementations
 */
interface FlowUseCase<ARGS, T> {

    /**
     * Function which builds Flow instance based on given arguments
     * @param args initial use case arguments
     */
    fun build(args: ARGS): Flow<T>
}
