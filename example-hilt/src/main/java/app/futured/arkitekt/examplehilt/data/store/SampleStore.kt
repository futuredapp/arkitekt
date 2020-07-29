package app.futured.arkitekt.examplehilt.data.store

import android.util.Log
import javax.inject.Inject

class SampleStore @Inject constructor() {

    fun getValue(): Int {
        return 5
    }
}
