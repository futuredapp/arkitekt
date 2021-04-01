package app.futured.arkitekt.examplehilt.tools

import androidx.lifecycle.SavedStateHandle

/**
 * Get mandatory value from [SavedStateHandle] or throw [IllegalArgumentException].
 */
fun <T> SavedStateHandle.getOrThrow(key: String): T =
    get<T>(key) ?: throw IllegalArgumentException("SavedStateHandle missing argument '$key'")
