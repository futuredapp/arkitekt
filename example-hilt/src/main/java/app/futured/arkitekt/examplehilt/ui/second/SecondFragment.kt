package app.futured.arkitekt.examplehilt.ui.second

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import app.futured.arkitekt.core.BaseView
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.event.Event
import app.futured.arkitekt.crusecases.BaseCrViewModel
import app.futured.arkitekt.sample.hilt.R
import dagger.Module
import javax.inject.Inject
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.SavedStateHandle
import app.futured.arkitekt.core.livedata.UiData
import app.futured.arkitekt.examplehilt.ui.base.BaseHiltFragment
import app.futured.arkitekt.sample.hilt.databinding.FragmentSecondBinding
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent

@AndroidEntryPoint
class SecondFragment : BaseHiltFragment<
    SecondViewModel,
    SecondViewState,
    FragmentSecondBinding>(),
    SecondView {

    override val viewModel: SecondViewModel by viewModels()

    val navViewModel: SecondViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override val layoutResId = R.layout.fragment_second

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeEvents()
    }

    private fun observeEvents() {}
}

sealed class SecondEvent : Event<SecondViewState>()
object NavigateBackEvent : SecondEvent()

@Module
@InstallIn(ActivityComponent::class)
class SecondFragmentModule

interface SecondView : BaseView

//@HiltViewModel
class SecondViewModel @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    override val viewState: SecondViewState,
) : BaseCrViewModel<SecondViewState>() {

    override fun onStart() {

    }
}

class SecondViewState @Inject constructor() : ViewState {

    val number = UiData("0")
}
