package app.futured.arkitekt.core.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UiDataTest {
    @Rule @JvmField val instantExecutorRule = InstantTaskExecutorRule()

    @Mock lateinit var observer: Observer<String>

    @Test
    fun `testUiDataInitFunction uiData()`() { // test per scenario
        val initVal = "1"

        val uiData = uiData(initVal)

        assert(initVal == uiData.value)
    }

    @Test
    fun `testUiDataInitFunction uiData { }`() { // test per scenario
        val initVal = "1"

        val uiData = uiData { initVal }

        assert(initVal == uiData.value)
    }

    @Test
    fun `testUiDataInitFunction constructor`() { // test per scenario
        val initVal = "1"

        val uiData = UiData(initVal)

        assert(initVal == uiData.value)
    }

    @Test
    fun `testUiDataInitFunction extension function`() { // test per scenario
        val initVal = "1"

        val uiData = initVal.toUiData()

        assert(initVal == uiData.value)
    }

    @Test
    fun testUiDataMutability() {

        val uiData = uiData("1")
        uiData.value = "2"

        uiData.observeForever(observer)
        assert("2" == uiData.value)
    }

    @Test
    fun testUiDataNonNull() {
        val uiData: UiData<String> = uiData("1")
        assert(uiData.value != null)
    }

    @Test
    fun `observer receives both initial data and data provided by setValue`() {
        val uiData = uiData("1")
        val observedValues = mutableListOf<String>()

        uiData.observeForever {
            observedValues.add(it)
        }

        uiData.value = "2"

        assert(observedValues.toString() == "[1, 2]")
    }

    @Test
    fun `test map`() {
        val uiData = uiData("four")
        val observedValues = mutableListOf<Int>()
        val mapResult: LiveData<Int> = uiData.map { it.length }
        mapResult.observeForever {
            observedValues.add(it)
        }
        uiData.value = "sixsix"
        uiData.value = "fourfour"

        assert(observedValues.toString() == "[4, 6, 8]")
    }

    @Test
    fun `test NonnullLiveData map`() {
        val uiData = NonNullLiveData("four")
        val observedValues = mutableListOf<Int>()

        val mapResult: LiveData<Int> = uiData
            .map { it.length }
            .map { it + it }

        mapResult.observeForever {
            observedValues.add(it)
        }

        assert(observedValues.toString() == "[8]")
    }
}
