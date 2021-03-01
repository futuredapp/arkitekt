package app.futured.arkitekt.examplehilt

import androidx.test.filters.MediumTest
import app.futured.arkitekt.core.viewmodel.ViewModelTest
import app.futured.arkitekt.examplehilt.data.store.SampleStore
import app.futured.arkitekt.examplehilt.domain.GetRandomNumberUseCase
import app.futured.arkitekt.examplehilt.tools.MainCoroutineRule
import app.futured.arkitekt.examplehilt.ui.first.FirstViewModel
import app.futured.arkitekt.examplehilt.ui.first.FirstViewState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
class FirstViewModelTest : ViewModelTest() {

    // Subject under test
    lateinit var viewModel: FirstViewModel
    lateinit var viewState: FirstViewState

    // mock and fake
    private val sampleStore: SampleStore = mockk()
    private lateinit var getRandomNumberUseCase: GetRandomNumberUseCase

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        getRandomNumberUseCase = GetRandomNumberUseCase(sampleStore)
        viewState = mockk(relaxed = true)
        viewModel = spyk(FirstViewModel(getRandomNumberUseCase, viewState), recordPrivateCalls = true)
    }

    @Test
    fun loadRandomNumberFromStore() {
        every { sampleStore.randomValue } returns 5

        Assert.assertEquals(5, viewModel.viewState.randomNumber.value)
    }

}
