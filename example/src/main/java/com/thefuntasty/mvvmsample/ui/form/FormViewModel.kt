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
        getFormFlowInteractor.execute({
            viewState.storedContent.value = "${it.first} ${it.second}"
        }, {
            Log.e("error", it.message, it)
        })
    }

    fun onSubmit() {
        saveFormInteractor.init(viewState.login.value to viewState.password.value).execute {
            sendEvent(ShowToastEvent("${it.first} ${it.second}"))
        }
    }

    fun onBack() = sendEvent(NavigateBackEvent)
}
