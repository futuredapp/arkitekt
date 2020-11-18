package app.futured.arkitekt.sample.ui.main

import android.os.Bundle
import app.futured.arkitekt.sample.R
import app.futured.arkitekt.sample.databinding.ActivityMainBinding
import app.futured.arkitekt.sample.ui.base.BaseActivity
import app.futured.arkitekt.sample.ui.bottomsheet.ExampleBottomSheetFragment
import app.futured.arkitekt.sample.ui.coroutinesresult.CoroutinesResultActivity
import app.futured.arkitekt.sample.ui.detail.DetailActivity
import app.futured.arkitekt.sample.ui.form.FormActivity
import app.futured.arkitekt.sample.ui.login.activity.LoginActivity
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, MainViewState, ActivityMainBinding>(), MainView {

    @Inject override lateinit var viewModelFactory: MainViewModelFactory

    override val layoutResId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeEvent(ShowDetailEvent::class) { startActivity(DetailActivity.getStartIntent(this)) }

        observeEvent(ShowFormEvent::class) { startActivity(FormActivity.getStartIntent(this)) }

        observeEvent(ShowLoginEvent::class) { startActivity(LoginActivity.getStartIntent(this)) }

        observeEvent(ShowBottomSheetEvent::class) { ExampleBottomSheetFragment.newInstance().show(supportFragmentManager) }

        observeEvent(ShowLoadEvent::class) { startActivity(CoroutinesResultActivity.getStartIntent(this)) }
    }
}
