package com.thefuntasty.mvvmsample.ui.main

import app.futured.arkitekt.core.viewmodel.ViewModelTest
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MainViewModelTest : ViewModelTest() {

    lateinit var viewState: MainViewState
    lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewState = MainViewState()
        viewModel = spyk(MainViewModel(viewState), recordPrivateCalls = true)
    }

    @Test
    fun `when onDetail is called then ShowDetailEvent is send`() {
        // WHEN
        viewModel.onDetail()

        // THEN
        verify { viewModel.sendEvent(ShowDetailEvent) }
    }

    @Test
    fun `when onForm is called then ShowFormEvent is send`() {
        // WHEN
        viewModel.onForm()

        // THEN
        verify { viewModel.sendEvent(ShowFormEvent) }
    }

    @Test
    fun `when onLogin is called then ShowLoginEvent is send`() {
        // WHEN
        viewModel.onLogin()

        // THEN
        verify { viewModel.sendEvent(ShowLoginEvent) }
    }

    @Test
    fun `when onBottomSheet is called then ShowBottomSheetEvent is send`() {
        // WHEN
        viewModel.onBottomSheet()

        // THEN
        verify { viewModel.sendEvent(ShowBottomSheetEvent) }
    }
}
