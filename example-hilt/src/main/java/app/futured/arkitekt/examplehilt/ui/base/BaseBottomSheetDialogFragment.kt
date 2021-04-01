package app.futured.arkitekt.examplehilt.ui.base

import androidx.databinding.ViewDataBinding
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.fragment.bottomsheet.BindingViewModelBottomSheetDialogFragment
import app.futured.arkitekt.sample.hilt.BR

abstract class BaseBottomSheetDialogFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BindingViewModelBottomSheetDialogFragment<VM, VS, B>() {

    override val brViewModelVariableId = BR.viewModel
    override val brViewStateVariableId = BR.viewState
    override val brViewVariableId = BR.view
}
