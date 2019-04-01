package com.thefuntasty.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.thefuntasty.mvvm.event.Event
import kotlin.reflect.KClass

abstract class ViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState> : Fragment(), ViewModelCreator<VM> {

    /**
     * Property which holds reference to layout identifier eg. R.layout.main_fragment. You should override this
     * in your specific Fragment implementation.
     */
    abstract val layoutResId: Int

    val viewModel: VM by lazy {
        getVM().apply {
            lifecycle.addObserver(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutResId, container, false)

    private fun getVM(): VM = ViewModelProviders.of(this, viewModelFactory).get(viewModelFactory.viewModelClass.java)

    inline fun <reified AVM : BaseViewModel<*>> getActivityViewModel(): AVM =
        ViewModelProviders.of(requireActivity()).get(AVM::class.java)

    @Suppress("UNCHECKED_CAST")
    fun <EVENT : Event<VS>> observeEvent(event: KClass<out EVENT>, observer: (EVENT) -> Unit) {
        viewModel.observeEvent(this, event, observer as (Event<VS>) -> Unit)
    }
}
