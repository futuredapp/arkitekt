package com.thefuntasty.mvvmsample.ui.coroutinesresult

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.thefuntasty.mvvmsample.R
import com.thefuntasty.mvvmsample.databinding.ActivityFormBinding
import com.thefuntasty.mvvmsample.ui.base.BaseActivity
import javax.inject.Inject

class CoroutinesResultActivity : BaseActivity<CoroutinesResultViewModel, CoroutinesResultViewState, ActivityFormBinding>(), CoroutinesResultView {

    @Inject override lateinit var viewModelFactory: CoroutinesResultViewModelFactory

    override val layoutResId = R.layout.activity_coroutines_result

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, CoroutinesResultActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeEvent(NavigateBackEvent::class) {
            onBackPressed()
        }
    }
}
