package app.futured.arkitekt.sample.ui.main

import androidx.savedstate.SavedStateRegistryOwner
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun savedStateRegistryOwner(activity: MainActivity): SavedStateRegistryOwner = activity
}
