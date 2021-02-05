package app.futured.arkitekt.examplehilt.domain

import app.futured.arkitekt.crusecases.UseCase
import app.futured.arkitekt.examplehilt.data.store.SampleStore
import javax.inject.Inject

class SampleUseCase @Inject constructor(
    private val sampleStore: SampleStore
) : UseCase<Unit, Int>() {

    override suspend fun build(args: Unit) = sampleStore.getValue()
}
