package app.futured.arkitekt.sample.ui.login.fragment

import android.os.Bundle
import android.view.View
import app.futured.arkitekt.sample.R
import app.futured.arkitekt.sample.databinding.FragmentLoginBinding
import app.futured.arkitekt.sample.ui.base.BaseFragment
import javax.inject.Inject
import app.futured.arkitekt.sample.ui.login.activity.LoginViewModel as ALoginViewModel

class LoginFragment : BaseFragment<LoginViewModel, LoginViewState, FragmentLoginBinding>(), LoginView {

    @Inject override lateinit var viewModelFactory: LoginViewModelFactory

    override val layoutResId = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(NotifyActivityEvent::class) {
            getActivityViewModel<ALoginViewModel>().sendToastEvent(it.message)
        }

        observeEvent(NavigateBackEvent::class) {
            requireActivity().onBackPressed()
        }
    }
}
