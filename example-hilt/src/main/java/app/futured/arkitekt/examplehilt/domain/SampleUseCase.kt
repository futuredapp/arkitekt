package app.futured.arkitekt.examplehilt.domain

import android.util.Log
import app.futured.arkitekt.examplehilt.data.store.SampleStore
import javax.inject.Inject

class SampleUseCase @Inject constructor(
    private val sampleStore: SampleStore
) {

    fun doSomething() {
        Log.i("SampleClass", "Do something " + sampleStore.getValue())
    }
}
