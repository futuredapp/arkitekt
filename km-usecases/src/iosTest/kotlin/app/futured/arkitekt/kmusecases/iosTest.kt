package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.test.Test
import kotlin.test.assertTrue

class KmUseCasesTest : Scope {

    private val job = SupervisorJob()

    override val coroutineScope: CoroutineScope = CoroutineScope(job + Dispatchers.Main)

    @Test
    fun testExample() {
        assertTrue(Greeting().greeting().contains("iOS"), "Check iOS is mentioned")
    }
}
