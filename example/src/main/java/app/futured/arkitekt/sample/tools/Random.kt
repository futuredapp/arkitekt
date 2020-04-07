package app.futured.arkitekt.sample.tools

fun randomError() {
    check(!listOf(false, false, true).random()) { "Random exception" }
}
