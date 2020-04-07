package com.thefuntasty.mvvm.dagger.fragment.dialog

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import com.thefuntasty.mvvm.dagger.inject.TestableAndroidInjection
import app.futured.arkitekt.core.fragment.dialog.ViewModelDialogFragment
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

abstract class BaseDaggerDialogFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    ViewModelDialogFragment<VM, VS>() {

    @Inject internal lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onAttach(context: Context) {
        TestableAndroidInjection.inject(this)
        super.onAttach(context)
    }
}
