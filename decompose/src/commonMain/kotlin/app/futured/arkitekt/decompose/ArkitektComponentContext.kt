package app.futured.arkitekt.decompose

import app.futured.arkitekt.decompose.navigation.NavigationResultRegistry
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.GenericComponentContext

/**
 * An Arkitekt-specific [GenericComponentContext].
 * Any custom application-specific [ComponentContext] should implement this interface.
 */
interface ArkitektComponentContext<T : Any> : GenericComponentContext<T> {

    /**
     * Application-wide store for durable, one-shot navigation results.
     *
     * To enable durable results, override this in your `AppComponentContext` implementation:
     * construct a single [NavigationResultRegistry] at the root (over the root component's
     * `StateKeeper`, which survives process death) and return that same instance from every child
     * context. By default it throws, so durability is opt-in.
     */
    val navigationResultRegistry: NavigationResultRegistry
        get() = error(
            "navigationResultRegistry is not configured. Override it in your AppComponentContext to " +
                "use durable navigation results.",
        )
}
