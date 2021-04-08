package app.futured.arkitekt.kmmsample

import app.futured.arkitekt.kmusecases.scope.Scope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class BaseViewModel : Scope {
    private val job = SupervisorJob()

    override val coroutineScope: CoroutineScope = CoroutineScope(job + Dispatchers.Main)

    fun onDestroy() {
        job.cancel()
    }
}
