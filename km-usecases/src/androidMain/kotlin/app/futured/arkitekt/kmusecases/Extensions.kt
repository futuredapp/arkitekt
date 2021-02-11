package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val mainUiContext: CoroutineContext = Dispatchers.Main

actual val workerDispatcher: CoroutineDispatcher = Dispatchers.IO

actual fun Any.ensureNotFrozen(): Unit = Unit
actual fun <T> T.freeze(): T = this
