package app.futured.arkitekt.dagger.fragment.dialog

import android.content.Context
import androidx.databinding.ViewDataBinding
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.fragment.dialog.BindingViewModelDialogFragment
import app.futured.arkitekt.dagger.inject.TestableAndroidInjection

abstract class BaseDaggerBindingDialogFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BindingViewModelDialogFragment<VM, VS, B>() {

    override fun onAttach(context: Context) {
        TestableAndroidInjection.inject(this)
        super.onAttach(context)
    }
}
