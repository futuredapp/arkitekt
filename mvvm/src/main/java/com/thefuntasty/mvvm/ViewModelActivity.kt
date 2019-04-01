package com.thefuntasty.mvvm

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.thefuntasty.mvvm.event.Event
import kotlin.reflect.KClass

abstract class ViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState> : AppCompatActivity(), ViewModelCreator<VM> {

    /**
     * Property which holds reference to layout identifier eg. R.layout.main_activity.
     * You should override this in your specific Activity implementation.
     */
    abstract val layoutResId: Int

    /**
     * Reference to Activity ViewModel
     */
    val viewModel: VM by lazy {
        getVM().apply {
            lifecycle.addObserver(this)
        }
    }

    private fun getVM(): VM = ViewModelProviders.of(this, viewModelFactory).get(viewModelFactory.viewModelClass.java)

    /**
     * Observe event defined by event class and run observer lambda whenever event is
     * received. This event class must be associated with current Activity ViewState.
     * @param event Observed event class
     * @param observer Lambda called whenever event is received
     */
    @Suppress("UNCHECKED_CAST")
    fun <E : Event<VS>> observeEvent(event: KClass<out E>, observer: (E) -> Unit) {
        viewModel.observeEvent(this, event, observer as (Event<VS>) -> Unit)
    }
}
