package app.futured.arkitekt.kmmsample.di.module

import app.futured.arkitekt.kmmsample.DatabaseManager

interface PersistenceModule {
    val dbManager: DatabaseManager
}
