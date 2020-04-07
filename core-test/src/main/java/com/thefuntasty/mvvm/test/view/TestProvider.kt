package com.thefuntasty.mvvm.test.view

import app.futured.arkitekt.core.BaseViewModel
import javax.inject.Provider

class TestProvider<VIEW_MODEL : BaseViewModel<*>>(private val viewModel: VIEW_MODEL) : Provider<VIEW_MODEL> {
    override fun get() = viewModel
}

fun <VIEW_MODEL : BaseViewModel<*>> VIEW_MODEL.asProvider(): Provider<VIEW_MODEL> = TestProvider(this)
