package app.futured.arkitekt.examplehilt.data.store

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class SampleStore @Inject constructor() {

    val randomValue: Int = Random.nextInt(0, 100)
}
