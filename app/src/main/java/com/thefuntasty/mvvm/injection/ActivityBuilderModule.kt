package com.thefuntasty.mvvm.injection

import com.thefuntasty.mvvm.ui.main.MainActivity
import com.thefuntasty.mvvm.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun MainActivity(): MainActivity
}
