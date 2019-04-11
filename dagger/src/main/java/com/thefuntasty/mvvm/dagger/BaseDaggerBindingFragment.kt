package com.thefuntasty.mvvm.dagger

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.BindingViewModelFragment
import com.thefuntasty.mvvm.ViewState
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseDaggerBindingFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BindingViewModelFragment<VM, VS, B>(), HasSupportFragmentInjector {

    @Inject internal lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector
}
