package com.thefuntasty.mvvm.crusecases

import kotlinx.coroutines.CancellationException

/**
 * A discriminated union that encapsulates successful outcome either with [Success] or with [Error]
 *
 * This class is adapted to kotlin coroutines so if [Error] contains [CancellationException] then this error is rethrown.
 */
sealed class Result<out VALUE : Any?> {
    override fun toString(): String {
        return when (this) {
            is Success -> "Success($value)"
            is Error -> "Error(${error})"
        }
    }

    operator fun component1(): VALUE? = if (this is Success) value else null

    operator fun component2(): Throwable? = if (this is Error) error else null
}

/**
 * Represents a successful [Result], containing a [value].
 */
class Success<VALUE : Any?>(val value: VALUE) : Result<VALUE>() {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || other !is Success<*>) {
            return false
        }
        if (value != other.value) {
            return false
        }

        return true
    }

    override fun hashCode() = value.hashCode()
}

/**
 * Represents a failed [Result], containing an [error].
 */
class Error(val error: Throwable) : Result<Nothing>() {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || other !is Error) {
            return false
        }
        if (error != other.error) {
            return false
        }

        return true
    }

    override fun hashCode() = error.hashCode()
}

// Functions

/**
 * Calls the specified function [block] and returns [Success] with encapsulated result if invocation was successful,
 * catching and returning [Error] with any thrown exception except [CancellationException].
 */
inline fun <VALUE> tryCatch(block: () -> VALUE): Result<VALUE> {
    return try {
        Success(block())
    } catch (error: CancellationException) {
        throw error
    } catch (error: Throwable) {
        Error(error)
    }
}

// Extensions

/**
 * Returns the encapsulated value if this instance represents [Success] or null
 * for encapsulated exception if it is [Error].
 *
 * If [Error.error] is [CancellationException] then this error is rethrown.
 */
fun <VALUE> Result<VALUE>.getOrNull(): VALUE? {
    return when (this) {
        is Success -> value
        is Error -> {
            if (error is CancellationException) {
                throw error
            }
            null
        }
    }
}

/**
 * Returns the encapsulated value if this instance represents [Success] or [defaultValue]
 * for encapsulated exception if it is [Error].
 *
 * If [Error.error] is [CancellationException] then this error is rethrown.
 */
fun <VALUE> Result<VALUE>.getOrDefault(defaultValue: VALUE): VALUE {
    return when (this) {
        is Success -> value
        is Error -> {
            if (error is CancellationException) {
                throw error
            }
            defaultValue
        }
    }
}

/**
 * Returns the encapsulated value if this instance represents [Success] or result of [onError]
 * for encapsulated exception if it is [Error].
 *
 * If [Error.error] is [CancellationException] then this error is rethrown.
 */
inline fun <VALUE> Result<VALUE>.getOrElse(onError: (Throwable) -> VALUE): VALUE {
    return when (this) {
        is Success -> value
        is Error -> {
            if (error is CancellationException) {
                throw error
            }
            onError(error)
        }
    }
}

/**
 * Returns the encapsulated value if this instance represents [Success] or
 * throws the encapsulated exception if it is [Error].
 *
 * If [Error.error] is not [CancellationException] then [doBeforeThrow] is called before throw.
 */
inline fun <VALUE> Result<VALUE>.getOrThrow(doBeforeThrow: (Throwable) -> Unit = {}): VALUE {
    return when (this) {
        is Success -> value
        is Error -> {
            if (error !is CancellationException) {
                doBeforeThrow(error)
            }
            throw error
        }
    }
}

/**
 * Returns the encapsulated value if this instance represents [Success] or
 * throws [CancellationException] with [Error.error] as its cause if it is [Error].
 *
 * This method should be used in the case when you want to safely cancel your coroutine from inside.
 *
 * If [Error.error] is not [CancellationException] then [doBeforeThrow] is called before throw.
 */
inline fun <VALUE> Result<VALUE>.getOrCancel(doBeforeThrow: (Throwable) -> Unit = {}): VALUE {
    return when (this) {
        is Success -> value
        is Error -> {
            if (error !is CancellationException) {
                doBeforeThrow(error)
            }
            throw CancellationException(message = "Cancellation caused by $error", cause = error)
        }
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to encapsulated value
 * if this instance represents [Success] or the original encapsulated exception if it is [Error].
 *
 * Note, that an exception thrown by [transform] function is rethrown by this function.
 * See [mapCatching] for an alternative that encapsulates exceptions.
 */
inline fun <VALUE, NEW_VALUE> Result<VALUE>.map(transform: (VALUE) -> NEW_VALUE): Result<NEW_VALUE> {
    return when (this) {
        is Success -> Success(transform(value))
        is Error -> this
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to encapsulated value
 * if this instance represents [Success] or the original encapsulated exception if it is [Error].
 *
 * Any exception thrown by [transform] function is caught, encapsulated as a failure and returned by this function.
 * See [map] for an alternative that rethrows exceptions.
 */
inline fun <VALUE, NEW_VALUE> Result<VALUE>.mapCatching(transform: (VALUE) -> NEW_VALUE): Result<NEW_VALUE> {
    return when (this) {
        is Success -> tryCatch { transform(value) }
        is Error -> this
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to encapsulated exception
 * if this instance represents [Error] or the original encapsulated value if it is [Success].
 *
 * If [Error.error] is [CancellationException] then this error is rethrown.
 *
 * Note, that an exception thrown by [transform] function is rethrown by this function.
 * See [recoverCatching] for an alternative that encapsulates exceptions.
 */
inline fun <VALUE> Result<VALUE>.recover(transform: (Throwable) -> VALUE): Result<VALUE> {
    return when (this) {
        is Error -> {
            if (error is CancellationException) {
                throw error
            }
            Success(transform(error))
        }
        is Success -> this
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to encapsulated exception
 * if this instance represents [Error] or the original encapsulated value if it is [Success].
 *
 * If [Error.error] is [CancellationException] then this error is rethrown.
 *
 * Any other exception thrown by [transform] function is caught, encapsulated as a failure and returned by this function.
 * See [recover] for an alternative that rethrows exceptions.
 */
inline fun <VALUE> Result<VALUE>.recoverCatching(transform: (Throwable) -> VALUE): Result<VALUE> {
    return when (this) {
        is Error -> {
            if (error is CancellationException) {
                throw error
            }
            tryCatch { transform(error) }
        }
        is Success -> this
    }
}

/**
 * Returns the the result of [onSuccess] for encapsulated value if this instance represents [Success]
 * or the result of [onError] function for encapsulated exception if it is [Error].
 *
 * If [Error.error] is [CancellationException] then this error is rethrown.
 */
inline fun <VALUE, NEW_VALUE> Result<VALUE>.fold(onSuccess: (VALUE) -> NEW_VALUE, onError: (Throwable) -> NEW_VALUE): NEW_VALUE {
    return when (this) {
        is Success -> onSuccess(value)
        is Error -> {
            if (error is CancellationException) {
                throw error
            }
            onError(error)
        }
    }
}
