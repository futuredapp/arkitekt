package app.futured.arkitekt.examplehilt

import android.util.Log
import androidx.multidex.MultiDexApplication
import app.futured.arkitekt.core.error.UseCaseErrorHandler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        UseCaseErrorHandler.globalOnErrorLogger = { error ->
            Log.d("UseCase error", "$error")
        }
    }
}
