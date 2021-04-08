package app.futured.arkitekt.kmusecases.scope

import kotlinx.coroutines.CoroutineScope

expect interface Scope {
    val coroutineScope: CoroutineScope
}
