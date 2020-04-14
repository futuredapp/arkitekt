package app.futured.arkitekt.sample.ui.login.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import app.futured.arkitekt.sample.R
import app.futured.arkitekt.sample.databinding.ActivityLoginBinding
import app.futured.arkitekt.sample.tools.ToastCreator
import app.futured.arkitekt.sample.ui.base.BaseActivity
import app.futured.arkitekt.sample.ui.login.fragment.LoginFragment
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
