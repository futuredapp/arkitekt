package com.thefuntasty.mvvm.delegate

import androidx.fragment.app.FragmentActivity
import com.thefuntasty.mvvm.BaseView
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewModelView
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.event.Event
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
interface ViewModelActivityDelegate<VM : BaseViewModel<VS>, VS : ViewState> : ViewModelView<VM>, BaseView {

    fun initViewModel(fragmentActivity: FragmentActivity): VM {
        return getViewModelFromProvider().apply { fragmentActivity.lifecycle.addObserver(this) }
    }

    fun <E : Event<VS>> FragmentActivity.observeEvent(event: KClass<out E>, observer: (E) -> Unit) {
        viewModel.observeEvent(this, event, observer as (Event<VS>) -> Unit)
    }
}
