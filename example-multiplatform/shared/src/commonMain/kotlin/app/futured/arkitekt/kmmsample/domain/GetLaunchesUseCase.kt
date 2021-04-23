package app.futured.arkitekt.kmmsample.domain

import app.futured.arkitekt.kmmsample.data.model.LaunchUi
import app.futured.arkitekt.kmmsample.data.repository.LaunchRepository
import app.futured.arkitekt.kmmsample.di.CommonGraph
import app.futured.arkitekt.kmusecases.freeze
import app.futured.arkitekt.kmusecases.usecase.UseCase

class GetLaunchesUseCase : UseCase<Unit, ListWrapper<LaunchUi>>() , LaunchRepository by CommonGraph {
    init {
        freeze()
    }
    override suspend fun build(arg: Unit): ListWrapper<LaunchUi> = ListWrapper(getLaunches())
}
