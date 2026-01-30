package app.futured.arkitekt.sample

import android.app.Application
import android.util.Log
import app.futured.arkitekt.core.error.UseCaseErrorHandler
import app.futured.arkitekt.sample.injection.DaggerApplicationComponent

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

}
