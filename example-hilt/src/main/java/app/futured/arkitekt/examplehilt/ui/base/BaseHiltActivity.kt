package app.futured.arkitekt.examplehilt.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.event.Event
import app.futured.arkitekt.sample.hilt.BR
import kotlin.reflect.KClass

abstract class BaseHiltActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)

        _binding = setupBindingView(this, layoutResId) {
            it.setVariable(brViewVariableId, this)
            it.setVariable(brViewModelVariableId, viewModel)
            it.setVariable(brViewStateVariableId, viewModel.viewState)
            it.lifecycleOwner = this
        }
    }

    private fun setupBindingView(fragmentActivity: FragmentActivity, layoutResId: Int, set: (B) -> Unit): B {
        return DataBindingUtil.setContentView<B>(fragmentActivity, layoutResId).also {
            set(it)
        }
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
