package com.thefuntasty.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.thefuntasty.mvvm.event.Event
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class ViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState> : Fragment(), ViewModelCreator<VM> {

    abstract val layoutResId: Int

    lateinit var viewModel: VM

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getVM(viewModelClass).apply {
            lifecycle.addObserver(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutResId, container, false)

    private fun getVM(vmClazz: KClass<VM>): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(vmClazz.java)
    }

    inline fun <VS : ViewState, reified AVM : BaseViewModel<VS>> getActivityViewModel(): AVM =
        ViewModelProviders.of(requireActivity()).get(AVM::class.java)

    fun <EVENT : Event<VS>> Fragment.observeEvent(event: KClass<out EVENT>, observer: (EVENT) -> Unit) {
        viewModel.observeEvent(this, event, observer as (Event<VS>) -> Unit)
    }
}
