package com.thefuntasty.mvvm.event

import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState

/**
 * One time event sent from ViewModel to Fragment/Activity via [BaseViewModel.sendEvent].
 * Event is tied directly to specific Fragment/Activity via its ViewState. Events are
 * delivered through LiveData based bus.
 *
 * Event is guaranteed to be delivered just once even screen rotation or a similar
 * operation is in progress.
 */
abstract class Event<T : ViewState>
