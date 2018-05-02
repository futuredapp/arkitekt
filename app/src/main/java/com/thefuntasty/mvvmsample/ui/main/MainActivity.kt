package com.thefuntasty.mvvmsample.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import com.thefuntasty.mvvmsample.databinding.ActivityMainBinding
import com.thefuntasty.mvvmsample.ui.base.BaseActivity
import com.thefuntasty.mvvmsample.ui.detail.DetailActivity
import com.thefuntasty.mvvmsample.ui.form.FormActivity
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, MainViewState, ActivityMainBinding>(), MainView {

    @Inject override lateinit var viewModelFactory: MainViewModelFactory

    override fun createViewModel(): MainViewModel = getViewModelFromProvider()

    override fun inflateBindingLayout(layoutInflater: LayoutInflater): ActivityMainBinding? {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeEvent(ShowDetailEvent::class) { startActivity(DetailActivity.getStartIntent(this)) }

        observeEvent(ShowFormEvent::class) { startActivity(FormActivity.getStartIntent(this)) }
    }
}
