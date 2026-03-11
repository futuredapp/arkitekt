package app.futured.arkitekt.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.GenericComponentContext

/**
 * An Arkitekt-specific [GenericComponentContext].
 * Any custom application-specific [ComponentContext] should implement this interface.
 */
interface ArkitektComponentContext<T : Any> : GenericComponentContext<T>
