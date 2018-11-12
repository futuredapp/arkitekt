package com.thefuntasty.mvvmsample.ui.login.fragment

import com.thefuntasty.mvvmsample.R
import com.thefuntasty.mvvmsample.databinding.FragmentLoginBinding
import com.thefuntasty.mvvmsample.ui.base.BaseFragment
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginViewModel, LoginViewState, FragmentLoginBinding>(), LoginView {

    @Inject override lateinit var viewModelFactory: LoginViewModelFactory

    override fun getLayoutResId() = R.layout.fragment_login
}
