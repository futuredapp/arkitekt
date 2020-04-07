package com.thefuntasty.mvvm.dagger.fragment.bottomsheet

import android.content.Context
import androidx.databinding.ViewDataBinding
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import com.thefuntasty.mvvm.dagger.inject.TestableAndroidInjection
import app.futured.arkitekt.core.fragment.bottomsheet.BindingViewModelBottomSheetDialogFragment

abstract class BaseDaggerBindingBottomSheetDialogFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BindingViewModelBottomSheetDialogFragment<VM, VS, B>() {

    override fun onAttach(context: Context) {
        TestableAndroidInjection.inject(this)
        super.onAttach(context)
    }
}
