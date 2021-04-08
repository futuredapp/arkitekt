package app.futured.arkitekt.kmmsample.domain

import app.futured.arkitekt.kmmsample.data.model.LaunchUi
import app.futured.arkitekt.kmmsample.data.repository.LaunchRepository
import app.futured.arkitekt.kmmsample.data.repository.PersistenceRepository
import app.futured.arkitekt.kmmsample.di.CommonGraph
import app.futured.arkitekt.kmusecases.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class ObserveLaunchesUseCase : FlowUseCase<Unit, ListWrapper<LaunchUi>>(),
    LaunchRepository by CommonGraph, PersistenceRepository by CommonGraph {
    override fun build(arg: Unit): Flow<ListWrapper<LaunchUi>> = merge(
        observePersistedLaunches(),
        fetchLaunches()
    )
        .map {
            val l = it.map {
                it.copy(
                    mission = it.mission.copy(
                        name = "1-${it.mission.name}"
                    )
                )
            }
            ListWrapper(l)
        }

    private fun fetchLaunches(): Flow<List<LaunchUi>> = flow {
        val launches = getLaunches()
        emit(launches)
        while (true) {
         kotlinx.coroutines.delay(1000)
         insertLaunches(launches)
        }
    }

    private fun observePersistedLaunches(): Flow<List<LaunchUi>> = observeLaunches()
}

data class ListWrapper<T: Any>(val list: List<T>)
