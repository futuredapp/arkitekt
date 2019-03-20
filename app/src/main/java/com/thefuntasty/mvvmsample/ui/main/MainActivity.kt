package com.thefuntasty.mvvmsample.ui.main

import android.os.Bundle
import com.thefuntasty.mvvmsample.R
import com.thefuntasty.mvvmsample.databinding.ActivityMainBinding
import com.thefuntasty.mvvmsample.ui.base.BaseActivity
import com.thefuntasty.mvvmsample.ui.detail.DetailActivity
import com.thefuntasty.mvvmsample.ui.form.FormActivity
import com.thefuntasty.mvvmsample.ui.login.activity.LoginActivity
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, MainViewState, ActivityMainBinding>(), MainView {

    @Inject override lateinit var viewModelFactory: MainViewModelFactory

    override val layoutResId = R.layout.activity_main
    override val viewModelClass = MainViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeEvent(ShowDetailEvent::class) { startActivity(DetailActivity.getStartIntent(this)) }

        observeEvent(ShowFormEvent::class) { startActivity(FormActivity.getStartIntent(this)) }

        observeEvent(ShowLoginEvent::class) { startActivity(LoginActivity.getStartIntent(this)) }
    }
}
