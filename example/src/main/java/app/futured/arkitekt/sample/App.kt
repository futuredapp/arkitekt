package app.futured.arkitekt.sample

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import app.futured.arkitekt.crusecases.error.UseCaseErrorHandler
import app.futured.arkitekt.sample.injection.DaggerApplicationComponent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    val appComponent by lazy {
        DaggerApplicationComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()

        UseCaseErrorHandler.globalOnErrorLogger = { error ->
            Log.d("UseCase error", "$error")
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
