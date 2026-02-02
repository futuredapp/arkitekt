package app.futured.arkitekt.core

import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.futured.arkitekt.core.testactivity.EmptyEvent
import app.futured.arkitekt.core.testactivity.TestActivity
import app.futured.arkitekt.core.testactivity.TestLegacyCoreViewModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class  BaseLegacyCoreViewModelTest {

    private lateinit var activityScenario: ActivityScenario<TestActivity>
    private lateinit var activity: TestActivity
    private lateinit var testLegacyCoreViewModel: TestLegacyCoreViewModel

    @Before
    fun initActivity() {
        activityScenario = ActivityScenario.launch(TestActivity::class.java).onActivity {
            activity = it
            testLegacyCoreViewModel = ViewModelProvider(it).get(TestLegacyCoreViewModel::class.java)
        }
    }

    @After
    fun destroyActivity() {
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun onStartCalled() {
        activityScenario.onActivity {
            Assert.assertEquals(1, ViewModelProvider(it).get(TestLegacyCoreViewModel::class.java).viewState.onStartCallCount)
        }
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun onStartCalledOnceOnly() {
        activityScenario.recreate()
        activityScenario.onActivity {
            Assert.assertEquals(1, testLegacyCoreViewModel.viewState.onStartCallCount)
        }
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun onClearedCalled() {
        var onClearedCallCount = 0
        testLegacyCoreViewModel.viewState.onClearedCallback = {
            onClearedCallCount++
        }
        activityScenario.recreate()
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
        Assert.assertEquals(1, onClearedCallCount)
    }

    @Test
    fun testEventReceived() {
        var eventReceived = false
        testLegacyCoreViewModel.observeEvent((activity as LifecycleOwner), EmptyEvent::class) { eventReceived = true }
        testLegacyCoreViewModel.sentEmptyEvent()
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
        Assert.assertTrue(eventReceived)
    }

    @Test
    fun testEventNotReceivedAfterOnDestroy() {
        var eventReceived = false
        testLegacyCoreViewModel.observeEvent((activity as LifecycleOwner), EmptyEvent::class) { eventReceived = true }
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
        testLegacyCoreViewModel.sentEmptyEvent()
        Assert.assertFalse(eventReceived)
    }

    @Test
    fun observeWithoutOwnerReceivesItem() {
        var liveDataReceived: Int? = null
        var defaultLiveDataReceived: Int? = null
        var mediatorLiveDataReceived: Int? = null

        testLegacyCoreViewModel.observerTestIntegerLiveData { liveDataReceived = it }
        testLegacyCoreViewModel.observeTestIntegerDefaultLiveData { defaultLiveDataReceived = it }
        testLegacyCoreViewModel.observeTestDefaultMediatorLiveData { mediatorLiveDataReceived = it }
        testLegacyCoreViewModel.viewState.testIntegerLiveData.value = 10
        testLegacyCoreViewModel.viewState.testIntegerLiveData.value = 20

        testLegacyCoreViewModel.viewState.testIntegerDefaultLiveData.value = 10
        testLegacyCoreViewModel.viewState.testIntegerDefaultLiveData.value = 20

        testLegacyCoreViewModel.viewState.testIntegerMediatorLiveData.value = 10
        testLegacyCoreViewModel.viewState.testIntegerMediatorLiveData.value = 20
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

        testLegacyCoreViewModel.observerTestIntegerLiveData { liveDataReceived = it }
        testLegacyCoreViewModel.observeTestIntegerDefaultLiveData { defaultLiveDataReceived = it }
        testLegacyCoreViewModel.observeTestDefaultMediatorLiveData { mediatorLiveDataReceived = it }
        activityScenario.moveToState(Lifecycle.State.DESTROYED)

        testLegacyCoreViewModel.viewState.testIntegerLiveData.value = 10
        testLegacyCoreViewModel.viewState.testIntegerLiveData.value = 20

        testLegacyCoreViewModel.viewState.testIntegerDefaultLiveData.value = 10
        testLegacyCoreViewModel.viewState.testIntegerDefaultLiveData.value = 20

        testLegacyCoreViewModel.viewState.testIntegerMediatorLiveData.value = 10
        testLegacyCoreViewModel.viewState.testIntegerMediatorLiveData.value = 20
        Assert.assertEquals(null, liveDataReceived)
        Assert.assertEquals(1, defaultLiveDataReceived)
        Assert.assertEquals(1, mediatorLiveDataReceived)
    }
}
