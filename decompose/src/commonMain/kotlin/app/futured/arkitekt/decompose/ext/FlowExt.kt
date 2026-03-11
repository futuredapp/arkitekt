package app.futured.arkitekt.decompose.ext

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Converts this [Flow] to Decompose [Value], updating it while provided [coroutineScope] is active.
 *
 * @param initial Initial value for the [Value] object
 * @param coroutineScope [CoroutineScope] for Flow collection.
 */
fun <T : Any> Flow<T>.collectAsValue(initial: T, coroutineScope: CoroutineScope): Value<T> =
    MutableValue(initial).also { mutable ->
        coroutineScope.launch {
            collect { newValue ->
                mutable.value = newValue
            }
        }
    }

/**
 * Mutates this [MutableStateFlow] using provided [transform] function.
 */
fun <T : Any> update(mutableStateFlow: MutableStateFlow<T>, transform: T.() -> T) {
    mutableStateFlow.value = transform(mutableStateFlow.value)
}
