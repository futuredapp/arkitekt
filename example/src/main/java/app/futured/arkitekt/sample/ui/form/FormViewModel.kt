package app.futured.arkitekt.sample.ui.form

import app.futured.arkitekt.crusecases.BaseViewModel
import app.futured.arkitekt.sample.domain.ObserveFormUseCase
import app.futured.arkitekt.sample.domain.SaveFormUseCase
import app.futured.arkitekt.sample.ui.compose.ExampleRoute
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel(assistedFactory = FormViewModel.Factory::class)
class FormViewModel @AssistedInject constructor(
    private val saveFormUseCase: SaveFormUseCase,
    private val observeFormUseCase: ObserveFormUseCase,
    override val viewState: FormViewState,
    @Assisted private val route: ExampleRoute.Form
) : BaseViewModel<FormViewState>() {

    init {
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

    @AssistedFactory
    interface Factory{
        fun create(route: ExampleRoute.Form): FormViewModel
    }
}
