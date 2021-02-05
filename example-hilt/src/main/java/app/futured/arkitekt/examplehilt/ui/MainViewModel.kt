package app.futured.arkitekt.examplehilt.ui

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import app.futured.arkitekt.crusecases.BaseCrViewModel
import app.futured.arkitekt.examplehilt.domain.SampleUseCase

class MainViewModel @ViewModelInject constructor(
    private val sampleUseCase: SampleUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
    override val viewState: MainViewState
) : BaseCrViewModel<MainViewState>() {

    override fun onStart() {
        sampleUseCase.execute {
            onSuccess {
                Log.d("## MainViewModel", "success = $it")
                viewState.number.value = it.toString()
            }
            onError {
                Log.e("## MainViewModel", "error", it)
                viewState.number.value = "error occurred"
            }
        }
    }
}
