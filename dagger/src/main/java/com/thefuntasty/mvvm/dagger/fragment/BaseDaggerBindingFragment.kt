package com.thefuntasty.mvvm.dagger.fragment

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.dagger.inject.TestableAndroidInjection
import com.thefuntasty.mvvm.fragment.BindingViewModelFragment
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

abstract class BaseDaggerBindingFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BindingViewModelFragment<VM, VS, B>() {

    @Inject internal lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onAttach(context: Context) {
        TestableAndroidInjection.inject(this)
        super.onAttach(context)
    }
}
