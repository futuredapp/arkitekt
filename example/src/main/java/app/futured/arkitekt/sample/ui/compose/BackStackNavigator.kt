package app.futured.arkitekt.sample.ui.compose

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

interface BackStackNavigator<T : NavKey> {
    fun onBack()

    fun onNavigate(destination: T)
}

class BackStackNavigatorImpl<T : NavKey>(
    private val backStack: NavBackStack<T>,
) : BackStackNavigator<T> {
    override fun onBack() {
        backStack.removeLastOrNull()
    }

    override fun onNavigate(destination: T) {
        backStack.add(destination)
    }
}
