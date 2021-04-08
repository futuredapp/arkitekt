package app.futured.arkitekt.kmusecases.scope

import kotlinx.coroutines.CoroutineScope

actual interface Scope {
    actual val coroutineScope: CoroutineScope
}
