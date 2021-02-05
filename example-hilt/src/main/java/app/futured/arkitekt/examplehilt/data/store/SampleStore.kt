package app.futured.arkitekt.examplehilt.data.store

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleStore @Inject constructor() {

    fun getValue(): Int {
        return 5
    }
}
