package app.futured.arkitekt.sample.data.store

import app.futured.arkitekt.sample.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStore @Inject constructor() {
    private val userState = MutableStateFlow(User.EMPTY)

    fun setUser(user: User) {
        userState.value = user
        // ... optionally persist user
    }

    fun getUser(): StateFlow<User> = userState
}
