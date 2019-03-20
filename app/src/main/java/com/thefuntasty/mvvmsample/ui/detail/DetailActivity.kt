package com.thefuntasty.mvvmsample.ui.detail

import android.content.Context
import android.content.Intent
import com.thefuntasty.mvvmsample.R
import com.thefuntasty.mvvmsample.databinding.ActivityDetailBinding
import com.thefuntasty.mvvmsample.ui.base.BaseActivity
import javax.inject.Inject

class DetailActivity : BaseActivity<DetailViewModel, DetailViewState, ActivityDetailBinding>(), DetailView {

    @Inject override lateinit var viewModelFactory: DetailViewModelFactory

    override val layoutResId = R.layout.activity_detail
    override val viewModelClass = DetailViewModel::class

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, DetailActivity::class.java)
    }
}
