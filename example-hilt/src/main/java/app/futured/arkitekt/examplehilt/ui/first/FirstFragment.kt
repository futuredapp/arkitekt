package app.futured.arkitekt.examplehilt.ui.first

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.lifecycle.ViewModelInject
import app.futured.arkitekt.core.BaseView
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.event.Event
import app.futured.arkitekt.crusecases.BaseCrViewModel
import app.futured.arkitekt.sample.hilt.R
import dagger.Module
import javax.inject.Inject
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.map
import app.futured.arkitekt.core.livedata.DefaultValueMediatorLiveData
import app.futured.arkitekt.examplehilt.domain.GetRandomNumberUseCase
import app.futured.arkitekt.examplehilt.tools.navigateTo
import app.futured.arkitekt.examplehilt.ui.base.BaseHiltFragment
import app.futured.arkitekt.sample.hilt.databinding.FragmentFirstBinding
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent

@AndroidEntryPoint
class FirstFragment : BaseHiltFragment<
    FirstViewModel,
    FirstViewState,
    FragmentFirstBinding>(),
    FirstView {

    override val viewModel: FirstViewModel by viewModels()

    val navViewModel: FirstViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override val layoutResId = R.layout.fragment_first

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeEvents()
    }

    private fun observeEvents() {
        observeEvent(NavigateToSecondFragmentEvent::class) {
            navigateTo(FirstFragmentDirections.navigateToSecondFragment(it.number))
        }
    }
}

sealed class FirstEvent : Event<FirstViewState>()
data class NavigateToSecondFragmentEvent(val number: Int) : FirstEvent()

@Module
@InstallIn(ActivityComponent::class)
class FirstFragmentModule

interface FirstView : BaseView

//@HiltViewModel
class FirstViewModel @ViewModelInject constructor(
    private val getRandomNumberUseCase: GetRandomNumberUseCase,
    override val viewState: FirstViewState,
) : BaseCrViewModel<FirstViewState>() {

    override fun onStart() {
        getRandomNumberUseCase.execute {
            onSuccess {
                Log.d("## MainViewModel", "success = $it")
                viewState.randomNumber.value = it
            }
            onError {
                Log.e("## MainViewModel", "error", it)
            }
        }
    }

    fun onNext() {
        sendEvent(NavigateToSecondFragmentEvent(viewState.randomNumber.value))
    }
}

class FirstViewState @Inject constructor() : ViewState {
    val randomNumber = DefaultValueMediatorLiveData(3)
    val displayText = randomNumber.map { "Random number: $it" }
}
