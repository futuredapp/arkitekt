package com.thefuntasty.mvvmsample.ui.form

import com.thefuntasty.mvvm.test.ViewModelTest
import com.thefuntasty.mvvmsample.domain.ObserveFormUseCase
import com.thefuntasty.mvvmsample.domain.SaveFormUseCase
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class FormViewModelTest : ViewModelTest() {

    val mockSaveFormUseCase: SaveFormUseCase = mockk()
    val mockObserveFormUseCase: ObserveFormUseCase = mockk()

    lateinit var viewState: FormViewState
    lateinit var viewModel: FormViewModel

    @Before
    fun setUp() {
        viewState = FormViewState()
        viewModel = FormViewModel(mockSaveFormUseCase, mockObserveFormUseCase, viewState)
    }

    @Test
    fun `when onStart is called then form is observed and set to the most actual value`() {
        // TODO(create a sample for use cr-usecases)
    }
}
