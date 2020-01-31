package com.thefuntasty.mvvm.dagger.fragment

import android.content.Context
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.dagger.inject.TestableAndroidInjection
import com.thefuntasty.mvvm.fragment.ViewModelFragment

abstract class BaseDaggerFragment<VM : BaseViewModel<VS>, VS : ViewState> :
    ViewModelFragment<VM, VS>() {

    override fun onAttach(context: Context) {
        TestableAndroidInjection.inject(this)
        super.onAttach(context)
    }
}
