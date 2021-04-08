package app.futured.arkitekt.kmusecases.tests

import app.futured.arkitekt.kmusecases.scope.Scope
import app.futured.arkitekt.kmusecases.testusecases.NoParamNoReturnUC
import app.futured.arkitekt.kmusecases.tools.TestBase
import kotlinx.coroutines.CoroutineScope
import kotlin.test.Test
import kotlin.test.assertEquals

class UseCaseTest : TestBase() {
    @Test
    fun testReturn() = runTest {
        val scope = object : Scope {
            override val coroutineScope: CoroutineScope
                get() = this@runTest
        }

        val uc = NoParamNoReturnUC()
        uc.apply {
            scope.execute(Unit) {
                onSuccess {
                    assertEquals(Unit, it)
                }
                onError {
                    print(it.message)
//                    expectUnreached()
                }
            }
            scope.execute(Unit) {
                onSuccess {
                    assertEquals(Unit, it)
                }
                onError {
                    expectUnreached()
                }
            }
        }

    }
}
