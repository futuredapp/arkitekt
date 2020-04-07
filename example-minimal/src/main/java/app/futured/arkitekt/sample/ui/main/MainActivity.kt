package app.futured.arkitekt.sample.ui.main

import app.futured.arkitekt.sample.R
import app.futured.arkitekt.sample.databinding.ActivityMainBinding
import app.futured.arkitekt.sample.ui.base.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, MainViewState, ActivityMainBinding>(), MainView {

    @Inject override lateinit var viewModelFactory: MainViewModelFactory

    override val layoutResId = R.layout.activity_main
}
