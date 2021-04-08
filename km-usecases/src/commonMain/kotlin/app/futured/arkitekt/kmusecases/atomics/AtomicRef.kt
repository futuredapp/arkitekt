package app.futured.arkitekt.kmusecases.atomics

expect class AtomicRef<V>(initialValue: V) {
    fun get(): V
    fun set(newValue: V)
}
