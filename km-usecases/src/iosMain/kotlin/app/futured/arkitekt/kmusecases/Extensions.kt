package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.SharedImmutable
import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.concurrent.freeze

@SharedImmutable
actual val mainUiContext: CoroutineContext = Dispatchers.Main

@SharedImmutable
actual val workerDispatcher: CoroutineDispatcher = Dispatchers.Default

actual fun Any.ensureNotFrozen(): Unit = ensureNeverFrozen()
actual fun <T> T.freeze(): T = freeze()
