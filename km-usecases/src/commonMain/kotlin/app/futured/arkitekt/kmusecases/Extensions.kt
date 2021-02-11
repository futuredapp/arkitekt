package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
expect val mainUiContext: CoroutineContext

@SharedImmutable
expect val workerDispatcher: CoroutineDispatcher

expect fun Any.ensureNotFrozen() : Unit
expect fun <T> T.freeze() : T
