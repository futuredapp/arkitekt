package com.thefuntasty.mvvmsample.ui.main

import com.thefuntasty.mvvmsample.R
import com.thefuntasty.mvvmsample.databinding.ActivityMainBinding
import com.thefuntasty.mvvmsample.ui.base.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, MainViewState, ActivityMainBinding>(), MainView {

    @Inject override lateinit var viewModelFactory: MainViewModelFactory

    override val layoutResId = R.layout.activity_main
}
