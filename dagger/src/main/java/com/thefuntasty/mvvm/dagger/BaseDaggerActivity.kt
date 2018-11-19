package com.thefuntasty.mvvm.dagger

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.BaseViewModelActivity
import com.thefuntasty.mvvm.ViewState
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseDaggerActivity<VM : BaseViewModel<VS>, VS : ViewState>
    : BaseViewModelActivity<VM, VS>(), HasSupportFragmentInjector {

    @Inject internal lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return supportFragmentInjector
    }
}
