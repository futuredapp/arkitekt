package com.thefuntasty.mvvmsample.ui.login.fragment

import android.view.View
import androidx.lifecycle.MutableLiveData
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.DefaultValueLiveData
import javax.inject.Inject

class LoginViewState @Inject constructor() : ViewState {
    val name = DefaultValueLiveData("")
    val surname = DefaultValueLiveData("")

    val fullName = MutableLiveData<String>()
    val showHeader = DefaultValueLiveData(View.INVISIBLE)
}
