package com.thefuntasty.mvvm.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
    fun testUiDataInitFunctions() { // test per scenario
        val initVal = "1"

        val uiData = uiData(initVal)
        val uiData2 = uiData { initVal }
        val uiData3 = UiData(initVal)
        val uiData4 = initVal.toUiData()

        assert(initVal == uiData.value)
        assert(initVal == uiData2.value)
        assert(initVal == uiData3.value)
        assert(initVal == uiData4.value)
    }

    @Test
    fun testUiDataMutability() {

        val uiData = uiData("1")
        uiData.value = "2"

        uiData.observeForever(observer)
        assert("2" == uiData.value)
        // assert(observer.onChanged()) test onChanged has been called once
    }

    @Test
    fun testUiDataNonNull() {
        val uiData = uiData("1")
        assert(uiData.value != null)
    }
}
