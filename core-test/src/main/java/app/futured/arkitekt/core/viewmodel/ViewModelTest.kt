package app.futured.arkitekt.core.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.futured.arkitekt.core.test.internal.CoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

/**
 * Base class for ViewModel testing.
 *
 * Sample test:
 *
 * class SampleViewModelTest : ViewModelTest() {
 *
 *     val mockViewState: SampleViewState = mockk()
 *
 *     lateinit var viewModel: SampleViewModel
 *
 *     @Before
 *     fun setUp() {
 *         viewModel = spyk(SampleViewModel(mockViewState), recordPrivateCalls = true)
 *     }
 *
 *     @Test
 *     fun `when ... then ...`() {
 *         // GIVEN
 *         ...
 *
 *         // WHEN
 *         ...
 *
 *         // THEN
 *         ...
 *     }
 * }
 */
open class ViewModelTest {

    /*
    class SampleViewModelTest : ViewModelTest() {

        val mockViewState: SampleViewState = mockk()

        lateinit var viewModel: SampleViewModel

        @Before
        fun setUp() {
            viewModel = spyk(SampleViewModel(mockViewState), recordPrivateCalls = true)
        }

        @Test
        fun `when ... then ...`() {
            // GIVEN
            ...

            // WHEN
            ...

            // THEN
            ...
        }
    }
    */

    /**
     * Swap background android executor with the one that executes task synchronously.
     * It allows to work with the live data.
     */
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    /**
     * Swap coroutine dispatcher with the one that is executed immediately.
     * It allows to work with the coroutines.
     */
    @ExperimentalCoroutinesApi
    @get:Rule var coroutineScopeRule = CoroutineScopeRule()
}
