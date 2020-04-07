package com.thefuntasty.mvvm.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewModelCreator
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.event.Event
import kotlin.reflect.KClass

/**
 * Base BottomSheetDialogFragment class with built-in ViewModel support
 */
abstract class ViewModelBottomSheetDialogFragment<VM : BaseViewModel<VS>, VS : ViewState> : BottomSheetDialogFragment(),
    ViewModelCreator<VM> {

    /**
     * Property which holds reference to layout identifier eg. R.layout.fragment_custom_bottomsheet.
     * You should override this in your specific BottomSheetDialogFragment implementation.
     */
    abstract val layoutResId: Int

    /**
     * Property which holds BottomSheetDialogFragment tag
     * You should override this in your specific BottomSheetDialogFragment implementation.
     */
    abstract val fragmentTag: String

    /**
     * Reference to Fragment ViewModel
     */
    val viewModel: VM by lazy {
        getVM().apply {
            lifecycle.addObserver(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutResId, container, false)

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, fragmentTag)

    private fun getVM(): VM = ViewModelProvider(this, viewModelFactory).get(viewModelFactory.viewModelClass.java)

    /**
     * Get reference to Activity ViewModel. Make sure correct VM class is specified.
     */
    inline fun <reified AVM : BaseViewModel<*>> getActivityViewModel(): AVM =
        ViewModelProvider(requireActivity()).get(AVM::class.java)

    /**
     * Observe event defined by event class and run observer lambda whenever event is
     * received. This event class must be associated with current BottomSheetDialogFragment ViewState.
     * @param event Observed event class
     * @param observer Lambda called whenever event is received
     */
    @Suppress("UNCHECKED_CAST")
    fun <EVENT : Event<VS>> observeEvent(event: KClass<out EVENT>, observer: (EVENT) -> Unit) {
        viewModel.observeEvent(this, event, observer as (Event<VS>) -> Unit)
    }
}
