package app.futured.arkitekt.kmusecases.atomics

import app.futured.arkitekt.kmusecases.freeze
import kotlin.native.concurrent.AtomicReference

actual class AtomicRef<V> actual constructor(initialValue: V) {
    private val atomicRef = AtomicReference(initialValue)

    actual fun get() = atomicRef.value
    actual fun set(newValue: V) {
        atomicRef.value = newValue.freeze()
    }
}
