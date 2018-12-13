package com.thefuntasty.mvvm.delegate

import androidx.fragment.app.Fragment
import com.thefuntasty.mvvm.BaseView
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewModelView
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.event.Event
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
interface ViewModelFragmentDelegate<VM : BaseViewModel<VS>, VS : ViewState> : ViewModelView<VM>, BaseView {

    fun initViewModel(fragment: Fragment): VM = createViewModel().apply { fragment.lifecycle.addObserver(this) }

    fun <EVENT : Event<VS>> Fragment.observeEvent(event: KClass<out EVENT>, observer: (EVENT) -> Unit) {
        viewModel.observeEvent(this, event, observer as (Event<VS>) -> Unit)
    }
}
