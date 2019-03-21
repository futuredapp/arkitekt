package com.thefuntasty.mvvm

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.thefuntasty.mvvm.event.Event
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class ViewModelActivity<VM : BaseViewModel<VS>, VS : ViewState> : AppCompatActivity(), ViewModelCreator<VM> {

    /**
     * Property which holds reference to layout identifier eg. R.layout.main_activity. You should override this
     * in your specific Activity implementation.
     */
    abstract val layoutResId: Int

    val viewModel: VM by lazy {
        getVM(viewModelClass).apply {
            lifecycle.addObserver(this)
        }
    }

    private fun getVM(vmClazz: KClass<VM>): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(vmClazz.java)
    }

    fun <E : Event<VS>> observeEvent(event: KClass<out E>, observer: (E) -> Unit) {
        viewModel.observeEvent(this, event, observer as (Event<VS>) -> Unit)
    }
}
