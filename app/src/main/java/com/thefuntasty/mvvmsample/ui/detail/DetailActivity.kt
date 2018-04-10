package com.thefuntasty.mvvmsample.ui.detail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.thefuntasty.mvvmsample.databinding.ActivityDetailBinding
import com.thefuntasty.mvvmsample.ui.base.BaseActivity
import javax.inject.Inject

class DetailActivity : BaseActivity<DetailViewModel, ActivityDetailBinding>(), DetailView {

    @Inject lateinit var viewModelFactory: DetailViewModelFactory

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, DetailActivity::class.java)
    }

    override fun createViewModel(): DetailViewModel = getViewModelFromProvider(viewModelFactory)

    override fun inflateBindingLayout(layoutInflater: LayoutInflater): ActivityDetailBinding? {
        return ActivityDetailBinding.inflate(layoutInflater)
    }
}
