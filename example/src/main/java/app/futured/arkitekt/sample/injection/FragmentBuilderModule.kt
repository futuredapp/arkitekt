package app.futured.arkitekt.sample.injection

import app.futured.arkitekt.sample.ui.bottomsheet.ExampleBottomSheetFragment
import app.futured.arkitekt.sample.ui.bottomsheet.ExampleBottomSheetFragmentModule
import app.futured.arkitekt.sample.ui.login.fragment.LoginFragment
import app.futured.arkitekt.sample.ui.login.fragment.LoginFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector(modules = [LoginFragmentModule::class])
    abstract fun loginFragment(): LoginFragment

    @ContributesAndroidInjector(modules = [ExampleBottomSheetFragmentModule::class])
    abstract fun exampleBottomSheetFragment(): ExampleBottomSheetFragment
}
