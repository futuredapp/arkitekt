package app.futured.arkitekt.examplehilt.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.event.Event
import app.futured.arkitekt.sample.hilt.BR
import kotlin.reflect.KClass

abstract class BaseHiltFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> : Fragment() {

    private val brViewVariableId = BR.view
    private val brViewModelVariableId = BR.viewModel
    private val brViewStateVariableId = BR.viewState

    /**
     * Property which holds reference to layout identifier eg. R.layout.main_activity.
     * You should override this in your specific Activity implementation.
     */
    abstract val layoutResId: Int

    /**
     * Property which holds reference to ViewModel of Activity.
     * You should override this in your specific Activity implementation.
     */
    abstract val viewModel: VM

    val binding: B
        get() = _binding
            ?: throw IllegalStateException("ViewDataBinding cannot be accessed before onCreate() method call.")

    private var _binding: B? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        lifecycle.addObserver(viewModel)
        return setupBindingView(inflater, container, layoutResId) {
            it.setVariable(brViewVariableId, this)
            it.setVariable(brViewModelVariableId, viewModel)
            it.setVariable(brViewStateVariableId, viewModel.viewState)
            it.lifecycleOwner = this.viewLifecycleOwner
            _binding = it
        }
    }

    private fun setupBindingView(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        layoutResId: Int,
        set: (B) -> Unit
    ): View {
        val binding = DataBindingUtil.inflate<B>(layoutInflater, layoutResId, container, false).also {
            set(it)
        }
        return binding.root
    }

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
