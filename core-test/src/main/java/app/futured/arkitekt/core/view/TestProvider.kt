package app.futured.arkitekt.core.view

import app.futured.arkitekt.core.BaseLegacyCoreViewModel
import javax.inject.Provider

class TestProvider<VIEW_MODEL : BaseLegacyCoreViewModel<*>>(private val viewModel: VIEW_MODEL) : Provider<VIEW_MODEL> {
    override fun get() = viewModel
}

fun <VIEW_MODEL : BaseLegacyCoreViewModel<*>> VIEW_MODEL.asProvider(): Provider<VIEW_MODEL> = TestProvider(this)
