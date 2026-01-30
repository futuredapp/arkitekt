package app.futured.arkitekt.sample.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.futured.arkitekt.sample.App
import app.futured.arkitekt.sample.ui.compose.MinimalApp
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            MinimalApp(viewModelFactory = viewModelFactory)
        }
    }
}
