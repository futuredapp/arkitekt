object ProjectSettings {
    const val applicationId = "app.futured.arkitekt.sample"
    const val compileSdk = 36
    const val targetSdk = 36
    const val minSdk = 23
    const val group = "app.futured.arkitekt"

    val version = System.getenv("VERSION_NAME") ?: "0.0.1-SNAPSHOT"
}
