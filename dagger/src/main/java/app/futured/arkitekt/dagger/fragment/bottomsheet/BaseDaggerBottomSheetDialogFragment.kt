package app.futured.arkitekt.dagger.fragment.bottomsheet

import android.content.Context
import androidx.databinding.ViewDataBinding
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.fragment.bottomsheet.ViewModelBottomSheetDialogFragment
import app.futured.arkitekt.dagger.inject.TestableAndroidInjection

abstract class BaseDaggerBottomSheetDialogFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    ViewModelBottomSheetDialogFragment<VM, VS>() {

    override fun onAttach(context: Context) {
        TestableAndroidInjection.inject(this)
        super.onAttach(context)
    }
}
