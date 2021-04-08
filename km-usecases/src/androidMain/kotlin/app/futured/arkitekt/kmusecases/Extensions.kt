package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

internal actual val mainUiContext: CoroutineContext = Dispatchers.Main

internal actual val workerDispatcher: CoroutineDispatcher = Dispatchers.IO

internal actual fun Any.ensureNotFrozen(): Unit = Unit
internal actual fun <T> T.freeze(): T = this
