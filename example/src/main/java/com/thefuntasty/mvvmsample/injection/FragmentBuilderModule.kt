package com.thefuntasty.mvvmsample.injection

import com.thefuntasty.mvvmsample.ui.login.fragment.LoginFragment
import com.thefuntasty.mvvmsample.ui.login.fragment.LoginFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector(modules = [LoginFragmentModule::class])
    abstract fun loginFragment(): LoginFragment
}
