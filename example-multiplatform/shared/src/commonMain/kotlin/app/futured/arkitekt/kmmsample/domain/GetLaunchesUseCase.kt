package app.futured.arkitekt.kmmsample.domain

import app.futured.arkitekt.kmmsample.data.model.LaunchUi
import app.futured.arkitekt.kmmsample.data.repository.LaunchRepository
import app.futured.arkitekt.kmmsample.di.CommonGraph
import app.futured.arkitekt.kmusecases.usecase.UseCase

class GetLaunchesUseCase : UseCase<Unit, List<LaunchUi>>() , LaunchRepository by CommonGraph {
    override suspend fun build(arg: Unit): List<LaunchUi> = getLaunches()
}
