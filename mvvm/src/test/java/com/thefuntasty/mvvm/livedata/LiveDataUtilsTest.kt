package com.thefuntasty.mvvm.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LiveDataUtilsTest {

    @JvmField @Rule val rule = InstantTaskExecutorRule()

    @Mock lateinit var observer: Observer<Boolean>

    @Test
    fun testLiveDataCombineTwoNotEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(liveData1, liveData2) { first, second ->
            first.isNotEmpty() && second.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"
        liveData2.value = "NotEmptyValue"

        assert(resultLiveData.value == true)
    }

    @Test
    fun testLiveDataCombineTwoEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(liveData1, liveData2) { first, second ->
            first.isNotEmpty() && second.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"

        assert(resultLiveData.value == false)
    }

    @Test
    fun testLiveDataCombineThreeNotEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val liveData3 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(liveData1, liveData2, liveData3) { first, second, third ->
            first.isNotEmpty() && second.isNotEmpty() && third.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"
        liveData2.value = "NotEmptyValue"
        liveData3.value = "NotEmptyValue"

        assert(resultLiveData.value == true)
    }

    @Test
    fun testLiveDataCombineThreeEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val liveData3 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(liveData1, liveData2, liveData3) { first, second, third ->
            first.isNotEmpty() && second.isNotEmpty() && third.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"

        assert(resultLiveData.value == false)
    }

    @Test
    fun testLiveDataCombineFourNotEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val liveData3 = DefaultValueLiveData("")
        val liveData4 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(liveData1, liveData2, liveData3, liveData4) { first, second, third, fourth ->
            first.isNotEmpty() && second.isNotEmpty() && third.isNotEmpty() && fourth.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"
        liveData2.value = "NotEmptyValue"
        liveData3.value = "NotEmptyValue"
        liveData4.value = "NotEmptyValue"

        assert(resultLiveData.value == true)
    }

    @Test
    fun testLiveDataCombineFourEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val liveData3 = DefaultValueLiveData("")
        val liveData4 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(liveData1, liveData2, liveData3, liveData4) { first, second, third, fourth ->
            first.isNotEmpty() && second.isNotEmpty() && third.isNotEmpty() && fourth.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"
        liveData2.value = "NotEmptyValue"

        assert(resultLiveData.value == false)
    }

    @Test
    fun testLiveDataCombineFiveNotEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val liveData3 = DefaultValueLiveData("")
        val liveData4 = DefaultValueLiveData("")
        val liveData5 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(
                liveData1, liveData2, liveData3, liveData4, liveData5) { first, second, third, fourth, fifth ->
            first.isNotEmpty() && second.isNotEmpty() && third.isNotEmpty() && fourth.isNotEmpty() && fifth.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"
        liveData2.value = "NotEmptyValue"
        liveData3.value = "NotEmptyValue"
        liveData4.value = "NotEmptyValue"
        liveData5.value = "NotEmptyValue"

        assert(resultLiveData.value == true)
    }

    @Test
    fun testLiveDataCombineFiveEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val liveData3 = DefaultValueLiveData("")
        val liveData4 = DefaultValueLiveData("")
        val liveData5 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(
                liveData1, liveData2, liveData3, liveData4, liveData5) { first, second, third, fourth, fifth ->
            first.isNotEmpty() && second.isNotEmpty() && third.isNotEmpty() && fourth.isNotEmpty() && fifth.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"
        liveData2.value = "NotEmptyValue"
        liveData3.value = "NotEmptyValue"

        assert(resultLiveData.value == false)
    }

    @Test
    fun testLiveDataCombineSixNotEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val liveData3 = DefaultValueLiveData("")
        val liveData4 = DefaultValueLiveData("")
        val liveData5 = DefaultValueLiveData("")
        val liveData6 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(
                liveData1, liveData2, liveData3, liveData4, liveData5, liveData6) { first, second, third, fourth, fifth, sixth ->
            first.isNotEmpty() && second.isNotEmpty() && third.isNotEmpty() && fourth.isNotEmpty() && fifth.isNotEmpty() && sixth.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"
        liveData2.value = "NotEmptyValue"
        liveData3.value = "NotEmptyValue"
        liveData4.value = "NotEmptyValue"
        liveData5.value = "NotEmptyValue"
        liveData6.value = "NotEmptyValue"

        assert(resultLiveData.value == true)
    }

    @Test
    fun testLiveDataCombineSixEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val liveData3 = DefaultValueLiveData("")
        val liveData4 = DefaultValueLiveData("")
        val liveData5 = DefaultValueLiveData("")
        val liveData6 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(
                liveData1, liveData2, liveData3, liveData4, liveData5, liveData6) { first, second, third, fourth, fifth, sixth ->
            first.isNotEmpty() && second.isNotEmpty() && third.isNotEmpty() && fourth.isNotEmpty() && fifth.isNotEmpty() && sixth.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"
        liveData2.value = "NotEmptyValue"
        liveData3.value = "NotEmptyValue"
        liveData4.value = "NotEmptyValue"

        assert(resultLiveData.value == false)
    }

    @Test
    fun testLiveDataCombineSevenNotEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val liveData3 = DefaultValueLiveData("")
        val liveData4 = DefaultValueLiveData("")
        val liveData5 = DefaultValueLiveData("")
        val liveData6 = DefaultValueLiveData("")
        val liveData7 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(
                liveData1, liveData2, liveData3, liveData4, liveData5, liveData6, liveData7) { first, second, third, fourth, fifth, sixth, seventh ->
            first.isNotEmpty() && second.isNotEmpty() && third.isNotEmpty() && fourth.isNotEmpty() && fifth.isNotEmpty() && sixth.isNotEmpty() && seventh.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"
        liveData2.value = "NotEmptyValue"
        liveData3.value = "NotEmptyValue"
        liveData4.value = "NotEmptyValue"
        liveData5.value = "NotEmptyValue"
        liveData6.value = "NotEmptyValue"
        liveData7.value = "NotEmptyValue"

        assert(resultLiveData.value == true)
    }

    @Test
    fun testLiveDataCombineSevenEmpty() {
        val liveData1 = DefaultValueLiveData("")
        val liveData2 = DefaultValueLiveData("")
        val liveData3 = DefaultValueLiveData("")
        val liveData4 = DefaultValueLiveData("")
        val liveData5 = DefaultValueLiveData("")
        val liveData6 = DefaultValueLiveData("")
        val liveData7 = DefaultValueLiveData("")
        val resultLiveData = combineLiveData(
                liveData1, liveData2, liveData3, liveData4, liveData5, liveData6, liveData7) { first, second, third, fourth, fifth, sixth, seventh ->
            first.isNotEmpty() && second.isNotEmpty() && third.isNotEmpty() && fourth.isNotEmpty() && fifth.isNotEmpty() && sixth.isNotEmpty() && seventh.isNotEmpty()
        }
        resultLiveData.observeForever(observer)

        liveData1.value = "NotEmptyValue"
        liveData2.value = "NotEmptyValue"
        liveData3.value = "NotEmptyValue"
        liveData4.value = "NotEmptyValue"

        assert(resultLiveData.value == false)
    }
}
