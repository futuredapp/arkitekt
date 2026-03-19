package app.futured.arkitekt.decompose.ext

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.StackNavigator

/**
 * The same as [StackNavigation.bringToFront] but does not recreate [configuration] if it's class is already on stack and
 * the classes are not equal.
 */
inline fun <C : Any> StackNavigator<C>.switchTab(configuration: C, crossinline onComplete: () -> Unit = {}) {
    navigate(
        transformer = { stack ->
            val existing = stack.find { it::class == configuration::class }
            if (existing != null) {
                stack.filterNot { it::class == configuration::class } + existing
            } else {
                stack + configuration
            }
        },
        onComplete = { _, _ -> onComplete() },
    )
}
