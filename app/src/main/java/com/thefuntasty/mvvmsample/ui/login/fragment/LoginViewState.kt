package com.thefuntasty.mvvmsample.ui.login.fragment

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData

class LoginViewState : ViewState {
    val name = DefaultValueLiveData("")
    val surname = DefaultValueLiveData("")

    val fullName = MutableLiveData<String>()
    val showHeader = DefaultValueLiveData(View.INVISIBLE)
}
