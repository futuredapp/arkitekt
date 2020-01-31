package com.thefuntasty.mvvm.dagger.activity

import android.os.Bundle
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.activity.ViewModelActivity
import com.thefuntasty.mvvm.dagger.inject.TestableAndroidInjection

abstract class BaseDaggerActivity<VM : BaseViewModel<VS>, VS : ViewState> :
    ViewModelActivity<VM, VS>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        TestableAndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}
