package com.thefuntasty.mvvmsample.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import com.thefuntasty.mvvmsample.databinding.ActivityMainBinding
import com.thefuntasty.mvvmsample.ui.ShowDetailEvent
import com.thefuntasty.mvvmsample.ui.base.BaseActivity
import com.thefuntasty.mvvmsample.ui.detail.DetailActivity
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), MainView {

    @Inject lateinit var viewModelFactory: MainViewModelFactory

    override fun createViewModel(): MainViewModel {
        return getViewModelFromProvider(viewModelFactory, MainViewModel::class)
    }

    override fun inflateBindingLayout(layoutInflater: LayoutInflater): ActivityMainBinding? {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observerEvent(ShowDetailEvent::class) {
            startActivity(DetailActivity.getStartIntent(this))
        }
    }
}
