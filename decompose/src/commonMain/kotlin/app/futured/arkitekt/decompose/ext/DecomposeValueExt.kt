package app.futured.arkitekt.decompose.ext

import app.futured.arkitekt.decompose.coroutines.ValueStateFlow
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow

/**
 * Converts this Decompose [Value] to Kotlin [StateFlow].
 *
 * @param T The type of the value.
 * @return A [StateFlow] emitting the values of this [Value].
 */
fun <T : Any> Value<T>.asStateFlow(): StateFlow<T> = ValueStateFlow(decomposeValue = this)
