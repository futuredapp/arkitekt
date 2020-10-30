package app.futured.arkitekt.sample.injection

import app.futured.arkitekt.sample.ui.coroutinesresult.CoroutinesResultActivity
import app.futured.arkitekt.sample.ui.coroutinesresult.CoroutinesResultActivityModule
import app.futured.arkitekt.sample.ui.detail.DetailActivity
import app.futured.arkitekt.sample.ui.detail.DetailActivityModule
import app.futured.arkitekt.sample.ui.form.FormActivity
import app.futured.arkitekt.sample.ui.form.FormActivityModule
import app.futured.arkitekt.sample.ui.login.activity.LoginActivity
import app.futured.arkitekt.sample.ui.login.activity.LoginActivityModule
import app.futured.arkitekt.sample.ui.main.MainActivity
import app.futured.arkitekt.sample.ui.main.MainActivityModule
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

    @ContributesAndroidInjector(modules = [CoroutinesResultActivityModule::class])
    abstract fun coroutinesResultActivity(): CoroutinesResultActivity
}
