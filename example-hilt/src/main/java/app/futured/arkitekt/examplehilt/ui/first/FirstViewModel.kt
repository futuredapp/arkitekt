package app.futured.arkitekt.examplehilt.ui.first

import android.util.Log
import app.futured.arkitekt.crusecases.BaseCrViewModel
import app.futured.arkitekt.examplehilt.domain.GetRandomNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(
    private val getRandomNumberUseCase: GetRandomNumberUseCase,
    override val viewState: FirstViewState,
) : BaseCrViewModel<FirstViewState>() {

    override fun onStart() {
        getRandomNumberUseCase.execute {
            onSuccess { viewState.randomNumber.value = it }
            onError { Log.e("## MainViewModel", "error", it) }
        }
    }

    fun onNext() {
        sendEvent(NavigateToSecondFragmentEvent(viewState.randomNumber.value))
    }

    fun onBottomSheet() {
        sendEvent(NavigateToBottomSheetEvent(viewState.randomNumber.value))
    }
}
