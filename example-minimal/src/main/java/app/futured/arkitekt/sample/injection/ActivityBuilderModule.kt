package app.futured.arkitekt.sample.injection

import app.futured.arkitekt.sample.ui.main.MainActivity
import app.futured.arkitekt.sample.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivity(): MainActivity
}
