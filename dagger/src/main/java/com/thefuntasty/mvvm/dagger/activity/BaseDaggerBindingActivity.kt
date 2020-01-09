package com.thefuntasty.mvvm.dagger.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.activity.BindingViewModelActivity
import com.thefuntasty.mvvm.dagger.inject.TestableAndroidInjection

abstract class BaseDaggerBindingActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BindingViewModelActivity<VM, VS, B>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        TestableAndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}
