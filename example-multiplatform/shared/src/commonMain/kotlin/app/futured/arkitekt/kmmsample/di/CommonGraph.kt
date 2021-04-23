package app.futured.arkitekt.kmmsample.di

import app.futured.arkitekt.kmmsample.ApiManager
import app.futured.arkitekt.kmmsample.DatabaseManager
import app.futured.arkitekt.kmmsample.data.repository.LaunchRepository
import app.futured.arkitekt.kmmsample.data.repository.PersistenceRepository

internal object CommonGraph : LaunchRepository, PersistenceRepository {
    override val apiManager: ApiManager by lazy { ApiManager() }
    override val dbManager: DatabaseManager by lazy { DatabaseManager() }
}
