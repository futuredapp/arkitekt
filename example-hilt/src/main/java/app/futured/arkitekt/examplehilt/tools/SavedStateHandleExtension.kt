package app.futured.arkitekt.examplehilt.tools

import androidx.lifecycle.SavedStateHandle

fun <T> SavedStateHandle.getOrThrow(key: String): T =
    get<T>(key) ?: throw IllegalArgumentException("SavedStateHandle missing argument '$key'")
