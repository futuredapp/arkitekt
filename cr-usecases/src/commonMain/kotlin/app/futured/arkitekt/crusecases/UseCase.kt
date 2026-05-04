package app.futured.arkitekt.crusecases

/**
 * Base Coroutine use case meant to use in [CoroutineScopeOwner] implementations
 */
interface UseCase<ARGS, T> {
    /**
     * Suspend function which should contain business logic
     */
    suspend fun build(args: ARGS): T
}
