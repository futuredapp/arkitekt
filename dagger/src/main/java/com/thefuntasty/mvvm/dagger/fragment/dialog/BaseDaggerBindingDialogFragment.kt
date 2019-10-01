package com.thefuntasty.mvvm.dagger.fragment.dialog

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.ViewState
import com.thefuntasty.mvvm.fragment.dialog.BindingViewModelDialogFragment
import dagger.android.support.AndroidSupportInjection

abstract class BaseDaggerBindingDialogFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BindingViewModelDialogFragment<VM, VS, B>() {

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}
