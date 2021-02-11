package app.futured.arkitekt.kmmsample

import com.rudolfhladik.arkitektexample.db.CommonDatabase

expect class Platform() {
    val platform: String
}

expect fun createDb() : CommonDatabase
