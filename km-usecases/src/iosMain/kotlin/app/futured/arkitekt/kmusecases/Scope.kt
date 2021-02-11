package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineScope

actual interface Scope {
    actual val coroutineScope: CoroutineScope
}
