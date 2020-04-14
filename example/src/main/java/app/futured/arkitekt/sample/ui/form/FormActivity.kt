package app.futured.arkitekt.sample.ui.form

import android.content.Context
import android.content.Intent
import android.os.Bundle
import app.futured.arkitekt.sample.R
import app.futured.arkitekt.sample.databinding.ActivityFormBinding
import app.futured.arkitekt.sample.tools.ToastCreator
import app.futured.arkitekt.sample.ui.base.BaseActivity
import javax.inject.Inject

class FormActivity : BaseActivity<FormViewModel, FormViewState, ActivityFormBinding>(), FormView {

    @Inject override lateinit var viewModelFactory: FormViewModelFactory
    @Inject lateinit var toastCreator: ToastCreator

    override val layoutResId = R.layout.activity_form

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, FormActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeEvent(ShowToastEvent::class) {
            toastCreator.showToast(this, it.message)
        }

        observeEvent(NavigateBackEvent::class) {
            onBackPressed()
        }
    }
}
