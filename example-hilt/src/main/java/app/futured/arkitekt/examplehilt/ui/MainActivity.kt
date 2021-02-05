package app.futured.arkitekt.examplehilt.ui

import app.futured.arkitekt.examplehilt.domain.SampleUseCase
import app.futured.arkitekt.examplehilt.ui.base.BaseActivity
import app.futured.arkitekt.sample.hilt.R
import app.futured.arkitekt.sample.hilt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, MainViewState, ActivityMainBinding>(), MainView {

    @Inject lateinit var sampleUseCase: SampleUseCase

    override val layoutResId = R.layout.activity_main

    override lateinit var viewModelFactory: MainViewModelFactory
}
