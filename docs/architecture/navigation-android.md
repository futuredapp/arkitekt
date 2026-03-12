# Navigation — Android

Arkitekt does not provide its own navigation library for the Android path. Use Jetpack Navigation with Compose or any other navigation solution you prefer.

## Example with Jetpack Navigation

```kotlin
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onNavigateToDetail = { navController.navigate("detail/$it") })
        }
        composable("detail/{id}") { backStackEntry ->
            DetailScreen(id = backStackEntry.arguments?.getString("id").orEmpty())
        }
    }
}
```

For a working implementation, see the **example** module in the [Arkitekt GitHub repository](https://github.com/futuredapp/arkitekt).
