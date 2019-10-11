package com.thefuntasty.mvvm.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.thefuntasty.mvvm.viewmodel.testactivity.EmptyEvent
import com.thefuntasty.mvvm.viewmodel.testactivity.TestActivity
import com.thefuntasty.mvvm.viewmodel.testactivity.TestViewModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaseViewModelTest {

    private lateinit var activityScenario: ActivityScenario<TestActivity>
    private lateinit var activity: TestActivity
    private lateinit var testViewModel: TestViewModel

    @Before
    fun initActivity() {
        activityScenario = ActivityScenario.launch(TestActivity::class.java).onActivity {
            activity = it
            testViewModel = ViewModelProviders.of(it).get(TestViewModel::class.java)
        }
    }

    @After
    fun destroyActivity() {
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun onStartCalled() {
        activityScenario.onActivity {
            Assert.assertEquals(1, ViewModelProviders.of(it).get(TestViewModel::class.java).viewState.onStartCallCount)
        }
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun onStartCalledOnceOnly() {
        activityScenario.recreate()
        activityScenario.onActivity {
            Assert.assertEquals(1, testViewModel.viewState.onStartCallCount)
        }
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun onClearedCalled() {
        var onClearedCallCount = 0
        testViewModel.viewState.onClearedCallback = {
            onClearedCallCount++
        }
        activityScenario.recreate()
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
        Assert.assertEquals(1, onClearedCallCount)
    }

    @Test
    fun testEventReceived() {
        var eventReceived = false
        testViewModel.observeEvent((activity as LifecycleOwner), EmptyEvent::class) { eventReceived = true }
        testViewModel.sentEmptyEvent()
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
        Assert.assertTrue(eventReceived)
    }

    @Test
    fun testEventNotReceivedAfterOnDestroy() {
        var eventReceived = false
        testViewModel.observeEvent((activity as LifecycleOwner), EmptyEvent::class) { eventReceived = true }
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
        testViewModel.sentEmptyEvent()
        Assert.assertFalse(eventReceived)
    }

    @Test
    fun observeWithoutOwnerReceivesItem() {
        var liveDataReceived: Int? = null
        var defaultLiveDataReceived: Int? = null
        var mediatorLiveDataReceived: Int? = null

        testViewModel.observerTestIntegerLiveData { liveDataReceived = it }
        testViewModel.observeTestIntegerDefaultLiveData { defaultLiveDataReceived = it }
        testViewModel.observeTestDefaultMediatorLiveData { mediatorLiveDataReceived = it }
        testViewModel.viewState.testIntegerLiveData.value = 10
        testViewModel.viewState.testIntegerLiveData.value = 20

        testViewModel.viewState.testIntegerDefaultLiveData.value = 10
        testViewModel.viewState.testIntegerDefaultLiveData.value = 20

        testViewModel.viewState.testIntegerMediatorLiveData.value = 10
        testViewModel.viewState.testIntegerMediatorLiveData.value = 20
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
        Assert.assertEquals(20, liveDataReceived)
        Assert.assertEquals(20, defaultLiveDataReceived)
        Assert.assertEquals(20, mediatorLiveDataReceived)
    }

    @Test
    fun observeWithoutOwnerNotReceivesItemAfterDestroy() {
        var liveDataReceived: Int? = null
        var defaultLiveDataReceived: Int? = null
        var mediatorLiveDataReceived: Int? = null

        testViewModel.observerTestIntegerLiveData { liveDataReceived = it }
        testViewModel.observeTestIntegerDefaultLiveData { defaultLiveDataReceived = it }
        testViewModel.observeTestDefaultMediatorLiveData { mediatorLiveDataReceived = it }
        activityScenario.moveToState(Lifecycle.State.DESTROYED)

        testViewModel.viewState.testIntegerLiveData.value = 10
        testViewModel.viewState.testIntegerLiveData.value = 20

        testViewModel.viewState.testIntegerDefaultLiveData.value = 10
        testViewModel.viewState.testIntegerDefaultLiveData.value = 20

        testViewModel.viewState.testIntegerMediatorLiveData.value = 10
        testViewModel.viewState.testIntegerMediatorLiveData.value = 20
        Assert.assertEquals(null, liveDataReceived)
        Assert.assertEquals(1, defaultLiveDataReceived)
        Assert.assertEquals(1, mediatorLiveDataReceived)
    }
}
