package com.thefuntasty.mvvmsample.ui.form

import android.util.Log
import com.thefuntasty.mvvm.crinteractors.BaseCrViewModel
import com.thefuntasty.mvvmsample.domain.GetFormFlowInteractor
import com.thefuntasty.mvvmsample.domain.SaveFormInteractor
import javax.inject.Inject

class FormViewModel @Inject constructor(
    private val saveFormInteractor: SaveFormInteractor,
    private val getFormFlowInteractor: GetFormFlowInteractor
) : BaseCrViewModel<FormViewState>() {

    override val viewState = FormViewState("", "")

    override fun onStart() {
        getFormFlowInteractor.execute(Unit, {
            viewState.storedContent.value = "${it.first} ${it.second}"
        }, {
            Log.e("error", it.message, it)
        })
    }

    fun onSubmit() {
        saveFormInteractor.execute(SaveFormInteractor.Data(viewState.login.value to viewState.password.value)) {
            sendEvent(ShowToastEvent("${it.first} ${it.second}"))
        }
    }

    fun onBack() = sendEvent(NavigateBackEvent)
}
