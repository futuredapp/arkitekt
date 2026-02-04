package app.futured.arkitekt.core.event

import app.futured.arkitekt.core.ViewState

/**
 * One-time event sent from ViewModel to Composable via [BaseCoreViewModel.sendEvent].
 * Event is tied directly to specific screen via its ViewState. Events are
 * delivered through Channel-based system.
 *
 * Event is guaranteed to be delivered just once even when screen rotation or a similar
 * operation is in progress.
 */
abstract class Event<T : ViewState>
