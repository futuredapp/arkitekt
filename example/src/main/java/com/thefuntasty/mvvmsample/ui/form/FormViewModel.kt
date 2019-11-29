package com.thefuntasty.mvvmsample.ui.form

import android.util.Log
import com.thefuntasty.mvvm.crinteractors.BaseCrViewModel
import com.thefuntasty.mvvmsample.domain.GetFormFlowUsecase
import com.thefuntasty.mvvmsample.domain.SaveFormUsecase
import javax.inject.Inject

class FormViewModel @Inject constructor(
    private val saveFormUsecase: SaveFormUsecase,
    private val getFormFlowUsecase: GetFormFlowUsecase
) : BaseCrViewModel<FormViewState>() {

    override val viewState = FormViewState("", "")

    override fun onStart() {
        getFormFlowUsecase.execute {
            onNext { viewState.storedContent.value = "${it.first} ${it.second}" }
            onError { Log.e("error", it.message, it) }
        }
    }

    fun onSubmit() {
        saveFormUsecase.execute(SaveFormUsecase.Data(viewState.login.value to viewState.password.value)) {
            onSuccess { sendEvent(ShowToastEvent("${it.first} ${it.second}")) }
        }
    }

    fun onBack() = sendEvent(NavigateBackEvent)
}
