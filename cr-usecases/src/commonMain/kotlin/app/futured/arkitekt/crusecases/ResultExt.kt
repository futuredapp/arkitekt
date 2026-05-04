package app.futured.arkitekt.crusecases

import kotlinx.coroutines.CancellationException

/**
 * Returns the encapsulated value if this instance represents `success` or
 * throws [CancellationException] with [Throwable] as its cause if it is `error`.
 *
 * This method should be used in the case when you want to safely cancel your coroutine from inside.
 *
 * If [Throwable] is not [CancellationException] then [doBeforeThrow] is called before throw.
 */
inline fun <VALUE> Result<VALUE>.getOrCancel(doBeforeThrow: (Throwable) -> Unit = {}): VALUE = fold(
    onSuccess = { value -> value },
    onFailure = { exception ->
        if (exception !is CancellationException) {
            doBeforeThrow(exception)
        }
        throw CancellationException(
            message = "Cancellation caused by $exception",
            cause = exception,
        )
    },
)
