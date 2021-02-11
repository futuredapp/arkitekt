package app.futured.arkitekt.kmusecases

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}
