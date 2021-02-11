package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineScope

expect interface Scope {
    val coroutineScope: CoroutineScope
}
