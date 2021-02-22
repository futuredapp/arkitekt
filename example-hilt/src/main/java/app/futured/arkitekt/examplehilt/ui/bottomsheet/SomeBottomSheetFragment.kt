package app.futured.arkitekt.examplehilt.ui.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import app.futured.arkitekt.examplehilt.ui.base.BaseBottomSheetDialogFragment
import app.futured.arkitekt.sample.hilt.R
import app.futured.arkitekt.sample.hilt.databinding.FragmentSomeBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SomeBottomSheetFragment :
    BaseBottomSheetDialogFragment<SomeViewModel, SomeViewState, FragmentSomeBottomSheetBinding>(),
    SomeView {

    companion object {
        fun newInstance(): SomeBottomSheetFragment = SomeBottomSheetFragment()
    }

    override val viewModel: SomeViewModel by viewModels()

    override val layoutResId = R.layout.fragment_some_bottom_sheet

    override val fragmentTag = this::class.java.name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(CloseEvent::class) { dismiss() }
    }
}
