package com.thefuntasty.mvvmsample.ui.login.fragment

import android.os.Bundle
import android.view.View
import com.thefuntasty.mvvm.livedata.observeNonNull
import com.thefuntasty.mvvmsample.R
import com.thefuntasty.mvvmsample.databinding.FragmentLoginBinding
import com.thefuntasty.mvvmsample.ui.base.BaseFragment
import javax.inject.Inject
import com.thefuntasty.mvvmsample.ui.login.activity.LoginViewModel as ALoginViewModel
import com.thefuntasty.mvvmsample.ui.login.activity.LoginViewState as ALoginViewState

class LoginFragment : BaseFragment<LoginViewModel, LoginViewState, FragmentLoginBinding>(), LoginView {

    @Inject override lateinit var viewModelFactory: LoginViewModelFactory

    override val layoutResId = R.layout.fragment_login
    override val viewModelClass = LoginViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.fullName.observeNonNull(this) { fullName ->
            getActivityViewModel<ALoginViewState, ALoginViewModel>().sendToastEvent(fullName)
        }
    }
}
