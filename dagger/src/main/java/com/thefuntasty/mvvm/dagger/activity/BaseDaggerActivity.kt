package com.thefuntasty.mvvm.dagger.activity

import android.os.Bundle
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.activity.ViewModelActivity
import dagger.android.AndroidInjection

abstract class BaseDaggerActivity<VM : BaseViewModel<VS>, VS : ViewState> :
    ViewModelActivity<VM, VS>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}
