package com.thefuntasty.mvvm.dagger.fragment

import android.content.Context
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import com.thefuntasty.mvvm.dagger.inject.TestableAndroidInjection
import app.futured.arkitekt.core.fragment.ViewModelFragment

abstract class BaseDaggerFragment<VM : BaseViewModel<VS>, VS : ViewState> :
    ViewModelFragment<VM, VS>() {

    override fun onAttach(context: Context) {
        TestableAndroidInjection.inject(this)
        super.onAttach(context)
    }
}
