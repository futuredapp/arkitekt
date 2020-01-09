package com.thefuntasty.mvvmsample.ui.login.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.thefuntasty.mvvmsample.R
import com.thefuntasty.mvvmsample.databinding.ActivityLoginBinding
import com.thefuntasty.mvvmsample.tools.ToastCreator
import com.thefuntasty.mvvmsample.ui.base.BaseActivity
import com.thefuntasty.mvvmsample.ui.login.fragment.LoginFragment
import javax.inject.Inject

class LoginActivity : BaseActivity<LoginViewModel, LoginViewState, ActivityLoginBinding>(), LoginView {

    @Inject override lateinit var viewModelFactory: LoginViewModelFactory
    @Inject lateinit var toastCreator: ToastCreator

    override val layoutResId = R.layout.activity_login

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.login_placeholder, LoginFragment())
                .commit()
        }

        observeEvent(ShowToastEvent::class) {
            toastCreator.showToast(this, it.message)
        }
    }
}
