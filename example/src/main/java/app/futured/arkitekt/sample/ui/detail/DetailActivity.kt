package app.futured.arkitekt.sample.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import app.futured.arkitekt.sample.R
import app.futured.arkitekt.sample.databinding.ActivityDetailBinding
import app.futured.arkitekt.sample.ui.base.BaseActivity
import javax.inject.Inject

class DetailActivity : BaseActivity<DetailViewModel, DetailViewState, ActivityDetailBinding>(), DetailView {

    @Inject override lateinit var viewModelFactory: DetailViewModelFactory

    override val layoutResId = R.layout.activity_detail

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, DetailActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeEvent(NavigateBackEvent::class) {
            onBackPressed()
        }
    }
}
