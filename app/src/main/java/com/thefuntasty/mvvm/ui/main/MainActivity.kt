package com.thefuntasty.mvvm.ui.main

import android.view.LayoutInflater
import com.thefuntasty.mvvm.databinding.ActivityMainBinding
import com.thefuntasty.mvvm.ui.base.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), MainView {

    @Inject lateinit var viewModelFactory: MainViewModelFactory

    override fun createViewModel(): MainViewModel {
        return getViewModelFromProvider(viewModelFactory, MainViewModel::class)
    }

    override fun inflateBindingLayout(layoutInflater: LayoutInflater): ActivityMainBinding? {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}
