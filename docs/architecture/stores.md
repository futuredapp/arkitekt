# Stores (repositories)

Stores follow the repository pattern. Each store handles a single entity's data logic.

Examples: `UserStore`, `OrderStore`, `DeviceStore`.

## Defining a store

```kotlin
@Singleton
class UserStore @Inject constructor() {
    private val userState = MutableStateFlow(User.EMPTY)

    fun setUser(user: User) {
        userState.value = user
    }

    fun getUser(): StateFlow<User> = userState
}
```

## Data layer role

Stores are injected into use cases, not into ViewModels or Components directly. Besides custom stores, Room `Dao`s and Retrofit/Ktor API interfaces serve the same role in the data layer.

## Wrapping store access with a use case

```kotlin
class ObserveUserFullNameUseCase @Inject constructor(
    private val userStore: UserStore,
) : FlowUseCase<Unit, String> {
    override fun build(args: Unit): Flow<String> =
        userStore.getUser().map { "${it.firstName} ${it.lastName}" }
}
```

This pattern is the same for both the Android and KMP paths.
