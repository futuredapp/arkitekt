package app.futured.arkitekt.sample.ui.coroutinesresult

import app.futured.arkitekt.compose.BaseViewModel
import app.futured.arkitekt.crusecases.execute
import app.futured.arkitekt.sample.domain.dummy.ConfirmDataSavedSuccessfullyUseCase
import app.futured.arkitekt.sample.domain.dummy.GetDataFromDeviceUseCase
import app.futured.arkitekt.sample.domain.dummy.SaveDataToFirstServerUseCase
import app.futured.arkitekt.sample.domain.dummy.SaveDataToSecondServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class CoroutinesResultViewModel @Inject constructor(
    private val getDataFromDeviceUseCase: GetDataFromDeviceUseCase,
    private val saveDataToFirstServerUseCase: SaveDataToFirstServerUseCase,
    private val saveDataToSecondServerUseCase: SaveDataToSecondServerUseCase,
    private val confirmDataSavedSuccessfullyUseCase: ConfirmDataSavedSuccessfullyUseCase,
    override val viewState: CoroutinesResultViewState
) : BaseViewModel<CoroutinesResultViewState>() {

    private companion object {
        const val RESULT_DELAY = 500L
    }

    fun onStartLoadingClicked() = launchWithHandler {
        showLoading()

        // If the use case fails then result of `getOrElse` is returned
        val deviceData: String = getDataFromDeviceUseCase.execute().getOrElse { "Default data" }
        setLoadingState(step = "1")

        // If the use case fails then `showError` is called and the coroutine is canceled with CancellationException
        val firstSave: String = saveDataToFirstServerUseCase.execute(deviceData)
            .getOrElse { error ->
                showError(step = "2")
                throw CancellationException(message = "Cancellation caused by $error", cause = error)
            }

        setLoadingState(step = "2")

        // If the use case fails then result of `recover` is returned
        val secondSave = saveDataToSecondServerUseCase.execute(deviceData)
            .map { "OK" }
            .recover { "Ignored error" }
            .getOrThrow()

        setLoadingState(step = "3")

//         The use case returns either (result, null) or (null, Throwable)
        val result = confirmDataSavedSuccessfullyUseCase.execute(firstSave to secondSave)

        setLoadingState(step = "4")
        delay(RESULT_DELAY)

        result.fold(
            onSuccess = { showResult(it) },
            onFailure = { showError("4") }
        )
    }

    fun onBack() = sendEvent(NavigateBackEvent)

    private fun setLoadingState(step: String) {
        viewState.contentState.value = CoroutinesResultViewState.State.LOADING
        viewState.contentStateDescription.value = "$step. step: OK"
    }

    private fun showLoading() {
        viewState.contentState.value = CoroutinesResultViewState.State.LOADING
        viewState.contentStateDescription.value = "Loading..."
    }

    private fun showResult(result: String) {
        viewState.contentState.value = CoroutinesResultViewState.State.RESULT
        viewState.contentStateDescription.value = result
    }

    private fun showError(step: String) {
        viewState.contentState.value = CoroutinesResultViewState.State.ERROR
        viewState.contentStateDescription.value = "$step. step: FAILED"
    }
}
