package app.futured.arkitekt.core.view

import android.app.Activity
import androidx.fragment.app.Fragment
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.dagger.inject.TestableAndroidInjection
import dagger.android.AndroidInjection

/**
 * Add a callback that is executed right after [AndroidInjection.inject].
 * This method is useful for manual dependency injection in tests.
 *
 * [mockBlock] will be called with started [InstantTaskExecutor] that will force immediate execution of tasks
 * in Android Architectural Components. This allows manual creation of [ViewState] classes.
 * Otherwise, it would throw an exception
 *
 * Usage:
 *
 * doAfterActivityInjection<SampleActivity> { activity ->
 *     val viewModelProvider = SampleViewModel(mockk(), mockk(), SampleViewState()).asProvider()
 *     activity.viewModelFactory = SampleViewModelFactory(viewModelProvider)
 *     activity.someInjectedClass = mockk()
 * }
 *
 * @param mockBlock lambda that will be executed right after [Activity] will be injected.
 */
inline fun <reified ACTIVITY> doAfterActivityInjection(crossinline mockBlock: (ACTIVITY) -> Unit) where ACTIVITY : Activity {
    TestableAndroidInjection.onActivityInject = { activity ->
        activity as? ACTIVITY ?: error("doAfterActivityInjection expects ${ACTIVITY::class} but ${activity::class} was received")
        InstantTaskExecutor().performNow {
            mockBlock(activity)
        }
    }
}

/**
 * Add a callback that is executed right after [AndroidInjection.inject].
 * This method is useful for manual dependency injection in tests.
 *
 * [mockBlock] will be called with started [InstantTaskExecutor] that will force immediate execution of tasks
 * in Android Architectural Components. This allows manual creation of [ViewState] classes.
 * Otherwise, it would throw an exception
 *
 * Usage:
 *
 * doAfterFragmentInjection<SampleFragment> { fragment ->
 *     val viewModelProvider = SampleViewModel(mockk(), mockk(), SampleViewState()).asProvider()
 *     fragment.viewModelFactory = SampleViewModelFactory(viewModelProvider)
 *     fragment.someInjectedClass = mockk()
 * }
 *
 * @param mockBlock lambda that will be executed right after [Fragment] will be injected.
 */
inline fun <reified FRAGMENT> doAfterFragmentInjection(crossinline mockBlock: (FRAGMENT) -> Unit)
    where FRAGMENT : Fragment {
    TestableAndroidInjection.onFragmentInject = { fragment ->
        fragment as? FRAGMENT ?: error("doAfterFragmentInjection expects ${FRAGMENT::class} but ${fragment::class} was received")
        InstantTaskExecutor().performNow {
            mockBlock(fragment)
        }
    }
}
