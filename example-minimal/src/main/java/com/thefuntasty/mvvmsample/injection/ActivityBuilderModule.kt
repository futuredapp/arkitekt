package com.thefuntasty.mvvmsample.injection

import com.thefuntasty.mvvmsample.ui.main.MainActivity
import com.thefuntasty.mvvmsample.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivity(): MainActivity
}
