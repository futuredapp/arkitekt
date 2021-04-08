package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal expect val mainUiContext: CoroutineContext

@SharedImmutable
internal expect val workerDispatcher: CoroutineDispatcher
internal expect fun Any.ensureNotFrozen() : Unit
internal expect fun <T> T.freeze() : T
