package com.thefuntasty.mvvmsample.injection

import com.thefuntasty.mvvmsample.ui.detail.DetailActivity
import com.thefuntasty.mvvmsample.ui.detail.DetailActivityModule
import com.thefuntasty.mvvmsample.ui.form.FormActivity
import com.thefuntasty.mvvmsample.ui.form.FormActivityModule
import com.thefuntasty.mvvmsample.ui.login.activity.LoginActivity
import com.thefuntasty.mvvmsample.ui.login.activity.LoginActivityModule
import com.thefuntasty.mvvmsample.ui.main.MainActivity
import com.thefuntasty.mvvmsample.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [DetailActivityModule::class])
    abstract fun detailActivity(): DetailActivity

    @ContributesAndroidInjector(modules = [FormActivityModule::class])
    abstract fun formActivity(): FormActivity

    @ContributesAndroidInjector(modules = [LoginActivityModule::class])
    abstract fun loginActivity(): LoginActivity
}
