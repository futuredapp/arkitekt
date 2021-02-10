package app.futured.arkitekt.examplehilt.ui.second

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import app.futured.arkitekt.core.BaseView
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.event.Event
import app.futured.arkitekt.crusecases.BaseCrViewModel
import app.futured.arkitekt.examplehilt.ui.base.BaseHiltFragment
import app.futured.arkitekt.sample.hilt.R
import app.futured.arkitekt.sample.hilt.databinding.FragmentSecondBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SecondFragment : BaseHiltFragment<
    SecondViewModel,
    SecondViewState,
    FragmentSecondBinding>(),
    SecondView {

    override val viewModel: SecondViewModel by viewModels()

    //val navViewModel: SecondViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override val layoutResId = R.layout.fragment_second

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeEvents()
    }

    private fun observeEvents() {}
}

sealed class SecondEvent : Event<SecondViewState>()
object NavigateBackEvent : SecondEvent()

interface SecondView : BaseView

//@HiltViewModel
class SecondViewModel @ViewModelInject constructor(
    @Assisted val handle: SavedStateHandle,
    override val viewState: SecondViewState
) : BaseCrViewModel<SecondViewState>() {

    override fun onStart() {
        viewState.handleNumber.value = handle.getOrThrow<Int>("number")
    }
}

class SecondViewState @Inject constructor() : ViewState {
    val handleNumber = MutableLiveData(0)
    val displayText = handleNumber.map { "Received by handle: $it" }
}

fun <T> SavedStateHandle?.getOrThrow(key: String): T =
    this?.get<T>(key) ?: throw IllegalArgumentException("missing argument '$key'")
