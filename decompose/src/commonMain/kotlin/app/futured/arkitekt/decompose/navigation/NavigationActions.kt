package app.futured.arkitekt.decompose.navigation

/**
 * Defines actions related to navigation within the [NavigationActionsProducer], eg. the Screen Component.
 * These actions can be used to trigger navigation events, such as navigating to a new screen or going back.
 */
interface NavigationActions

/**
 * An interface representing a producer of navigation actions.
 *
 * Implementations of this interface provide access to a [navigation] class which
 * encapsulates various navigation actions specific to the implementing context (in standard case - the screen Component).
 *
 * @param NAV The type of the navigation actions object provided by this producer.
 */
interface NavigationActionsProducer<NAV : NavigationActions> {
    val navigation: NAV
}
