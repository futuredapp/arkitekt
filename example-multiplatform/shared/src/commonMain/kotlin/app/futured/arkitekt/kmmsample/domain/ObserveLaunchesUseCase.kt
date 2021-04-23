package app.futured.arkitekt.kmmsample.domain

import app.futured.arkitekt.kmmsample.data.model.LaunchUi
import app.futured.arkitekt.kmmsample.data.repository.LaunchRepository
import app.futured.arkitekt.kmmsample.data.repository.PersistenceRepository
import app.futured.arkitekt.kmmsample.di.CommonGraph
import app.futured.arkitekt.kmusecases.freeze
import app.futured.arkitekt.kmusecases.usecase.FlowUseCase
import io.ktor.utils.io.preventFreeze
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class ObserveLaunchesUseCase : FlowUseCase<Unit, ListWrapper<LaunchUi>>(),
    LaunchRepository by CommonGraph, PersistenceRepository by CommonGraph {
    init {
        freeze()
    }
    override fun build(arg: Unit): Flow<ListWrapper<LaunchUi>> = merge(
        observePersistedLaunches(),
        fetchLaunches()
    )
        .map { ListWrapper(it) }

    private fun fetchLaunches(): Flow<List<LaunchUi>> = flow {
        val launches = getLaunches()
        emit(launches)
        insertLaunches(launches)
    }

    private fun observePersistedLaunches(): Flow<List<LaunchUi>> = observeLaunches()
}

data class ListWrapper<T : Any>(val list: List<T>)
