package app.futured.arkitekt.sample.ui.coroutinesresult

import app.futured.arkitekt.core.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class CoroutinesResultViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<CoroutinesResultViewModel>
) : BaseViewModelFactory<CoroutinesResultViewModel>() {
    override val viewModelClass = CoroutinesResultViewModel::class
}
