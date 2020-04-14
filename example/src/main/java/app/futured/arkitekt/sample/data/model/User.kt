package app.futured.arkitekt.sample.data.model

data class User(val firstName: String, val lastName: String) {

    companion object {
        val EMPTY = User("", "")
    }
}
