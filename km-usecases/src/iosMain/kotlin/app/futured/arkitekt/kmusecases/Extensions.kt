package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.SharedImmutable
import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.concurrent.freeze

@SharedImmutable
internal actual val mainUiContext: CoroutineContext = Dispatchers.Main

@SharedImmutable
internal actual val workerDispatcher: CoroutineDispatcher = Dispatchers.Default
internal actual fun Any.ensureNotFrozen(): Unit = ensureNeverFrozen()
actual fun <T> T.freeze(): T = freeze()
