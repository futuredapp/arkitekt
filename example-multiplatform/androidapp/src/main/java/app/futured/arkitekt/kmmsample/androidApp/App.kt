package app.futured.arkitekt.kmmsample.androidApp

import android.app.Application
import app.futured.arkitekt.kmmsample.appContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}
