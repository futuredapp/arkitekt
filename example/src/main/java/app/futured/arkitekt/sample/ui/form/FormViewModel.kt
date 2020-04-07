package app.futured.arkitekt.sample.ui.form

import app.futured.arkitekt.crusecases.BaseCrViewModel
import app.futured.arkitekt.sample.domain.ObserveFormUseCase
import app.futured.arkitekt.sample.domain.SaveFormUseCase
import javax.inject.Inject

class FormViewModel @Inject constructor(
    private val saveFormUseCase: SaveFormUseCase,
    private val observeFormUseCase: ObserveFormUseCase,
    override val viewState: FormViewState
) : BaseCrViewModel<FormViewState>() {

    override fun onStart() {
        observeFormUseCase.execute {
            onNext { viewState.storedContent.value = "${it.first} ${it.second}" }
            onError { sendEvent(ShowToastEvent("Error :-(")) }
        }
    }

    fun onSubmit() {
        saveFormUseCase.execute(SaveFormUseCase.Data(viewState.login.value to viewState.password.value)) {
            onSuccess { sendEvent(ShowToastEvent("${it.first} ${it.second}")) }
        }
    }

    fun onBack() = sendEvent(NavigateBackEvent)
}
