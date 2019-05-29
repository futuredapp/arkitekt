package com.thefuntasty.mvvmsample.ui.bottomsheet

import android.os.Bundle
import android.view.View
import com.thefuntasty.mvvmsample.R
import com.thefuntasty.mvvmsample.databinding.FragmentExampleBinding
import com.thefuntasty.mvvmsample.ui.base.BaseBottomSheetDialogFragment
import javax.inject.Inject

class ExampleBottomSheetFragment : BaseBottomSheetDialogFragment<ExampleViewModel, ExampleViewState, FragmentExampleBinding>(),
    ExampleView {

    companion object {
        fun newInstance(): ExampleBottomSheetFragment = ExampleBottomSheetFragment()
    }

    @Inject override lateinit var viewModelFactory: ExampleViewModelFactory

    override val layoutResId = R.layout.fragment_example

    override val fragmentTag = "EXAMPLE_BOTTOM_SHEET"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(CloseEvent::class) { dismiss() }
    }
}
