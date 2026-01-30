package app.futured.arkitekt.sample.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.futured.arkitekt.sample.App
import app.futured.arkitekt.sample.tools.ToastCreator
import app.futured.arkitekt.sample.ui.bottomsheet.ExampleViewModelFactory
import app.futured.arkitekt.sample.ui.compose.Dependencies
import app.futured.arkitekt.sample.ui.compose.ExampleApp
import app.futured.arkitekt.sample.ui.coroutinesresult.CoroutinesResultViewModelFactory
import app.futured.arkitekt.sample.ui.detail.DetailViewModelFactory
import app.futured.arkitekt.sample.ui.form.FormViewModelFactory
import app.futured.arkitekt.sample.ui.login.activity.LoginViewModelFactory as LoginActivityViewModelFactory
import app.futured.arkitekt.sample.ui.login.fragment.LoginViewModelFactory as LoginFragmentViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject lateinit var mainViewModelFactory: MainViewModelFactory
    @Inject lateinit var detailViewModelFactory: DetailViewModelFactory
    @Inject lateinit var formViewModelFactory: FormViewModelFactory
    @Inject lateinit var loginActivityViewModelFactory: LoginActivityViewModelFactory
    @Inject lateinit var loginFragmentViewModelFactory: LoginFragmentViewModelFactory
    @Inject lateinit var coroutinesResultViewModelFactory: CoroutinesResultViewModelFactory
    @Inject lateinit var exampleViewModelFactory: ExampleViewModelFactory
    @Inject lateinit var toastCreator: ToastCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            ExampleApp(
                Dependencies(
                    mainViewModelFactory = mainViewModelFactory,
                    detailViewModelFactory = detailViewModelFactory,
                    formViewModelFactory = formViewModelFactory,
                    loginActivityViewModelFactory = loginActivityViewModelFactory,
                    loginFragmentViewModelFactory = loginFragmentViewModelFactory,
                    coroutinesResultViewModelFactory = coroutinesResultViewModelFactory,
                    exampleViewModelFactory = exampleViewModelFactory,
                    toastCreator = toastCreator,
                )
            )
        }
    }
}
