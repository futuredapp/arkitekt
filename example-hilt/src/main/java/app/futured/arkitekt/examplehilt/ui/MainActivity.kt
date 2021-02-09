package app.futured.arkitekt.examplehilt.ui

import androidx.activity.viewModels
import app.futured.arkitekt.examplehilt.ui.base.BaseHiltActivity
import app.futured.arkitekt.sample.hilt.R
import app.futured.arkitekt.sample.hilt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseHiltActivity<MainViewModel, MainViewState, ActivityMainBinding>(), MainView {

    override val layoutResId = R.layout.activity_main

    override val viewModel: MainViewModel by viewModels()
}
