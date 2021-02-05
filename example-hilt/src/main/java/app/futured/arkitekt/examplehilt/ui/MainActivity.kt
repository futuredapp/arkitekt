package app.futured.arkitekt.examplehilt.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import app.futured.arkitekt.core.livedata.observeNonNull
import app.futured.arkitekt.examplehilt.ui.base.BaseHiltActivity
import app.futured.arkitekt.sample.hilt.R
import app.futured.arkitekt.sample.hilt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseHiltActivity<MainViewModel, MainViewState, ActivityMainBinding>(), MainView {

    override val layoutResId = R.layout.activity_main

    override val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewState.number.observeNonNull(this) {
            Log.d("## MainActivity", "viewState.number = $it")
        }
    }
}
