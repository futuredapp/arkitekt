package app.futured.arkitekt.core.view

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor

/**
 * Class that swaps the background executor used by the Architecture Components with a
 * different one which executes each task synchronously.
 *
 * This class can be used for tests that use Architecture Components.
 */
@PublishedApi
@SuppressLint("RestrictedApi")
internal class InstantTaskExecutor {

    /**
     * Wrap invocation of [block] with immediate task execution
     */
    fun performNow(block: () -> Unit) {
        startExecuteTaskImmediately()
        block()
        stopExecuteTaskImmediately()
    }

    private fun startExecuteTaskImmediately() {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }

    private fun stopExecuteTaskImmediately() {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}
