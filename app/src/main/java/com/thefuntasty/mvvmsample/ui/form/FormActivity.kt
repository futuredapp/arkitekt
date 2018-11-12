package com.thefuntasty.mvvmsample.ui.form

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.thefuntasty.mvvmsample.R
import com.thefuntasty.mvvmsample.databinding.ActivityFormBinding
import com.thefuntasty.mvvmsample.ui.base.BaseActivity
import javax.inject.Inject

class FormActivity : BaseActivity<FormViewModel, FormViewState, ActivityFormBinding>(), FormView {

    @Inject override lateinit var viewModelFactory: FormViewModelFactory

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, FormActivity::class.java)
    }

    override fun getLayoutResId() = R.layout.activity_form

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeEvent(ShowToastEvent::class) {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}
