package app.futured.arkitekt.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewModelCreator
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.event.Event
import kotlin.reflect.KClass

/**
 * Base Fragment class with built-in ViewModel support
 */
abstract class ViewModelFragment<VM : BaseViewModel<VS>, VS : ViewState> : Fragment(), ViewModelCreator<VM> {

    /**
     * Property which holds reference to layout identifier eg. R.layout.main_fragment.
     * You should override this in your specific Fragment implementation.
     */
    abstract val layoutResId: Int

    /**
     * Reference to Fragment ViewModel
     */
    val viewModel: VM by lazy {
        getVM().apply {
            lifecycle.addObserver(this)
        }
    }

    /**
     * Reference to Fragment ViewState
     */
    val viewState: VS get() {
        return viewModel.viewState
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutResId, container, false)

    private fun getVM(): VM = ViewModelProvider(this, viewModelFactory).get(viewModelFactory.viewModelClass.java)

    /**
     * Get reference to Activity ViewModel. Make sure correct VM class is
     * specified.
     */
    inline fun <reified AVM : BaseViewModel<*>> getActivityViewModel(): AVM =
        ViewModelProvider(requireActivity()).get(AVM::class.java)

    /**
     * Observe event defined by event class and run observer lambda whenever event is
     * received. This event class must be associated with current Fragment ViewState.
     * @param event Observed event class
     * @param observer Lambda called whenever event is received
     */
    @Suppress("UNCHECKED_CAST")
    fun <EVENT : Event<VS>> observeEvent(event: KClass<out EVENT>, observer: (EVENT) -> Unit) {
        viewModel.observeEvent(this, event, observer as (Event<VS>) -> Unit)
    }
}
