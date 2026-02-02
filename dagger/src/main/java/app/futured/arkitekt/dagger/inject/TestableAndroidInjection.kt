package app.futured.arkitekt.dagger.inject

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * AndroidInjection wrapper that allows customizing dependency injection of Activity or Fragment.
 *
 * Important: Methods and properties of this object should be used only for testing purposes.
 */
object TestableAndroidInjection {

    /**
     * Callback invoked when [Activity] class is being injected.
     * It should be used in tests for manual dependency injection of [Activity] class.
     *
     * The most common use case is to replace [ViewModelCreator.viewModelFactory] with the factory
     * that provides a view model with mocked use cases
     */
    var onActivityInject: ((Activity) -> Unit)? = null

    /**
     * Callback invoked when [Fragment] class is being injected.
     * It should be used in tests for manual dependency injection of [Fragment] class.
     *
     * The most common use case is to replace [ViewModelCreator.viewModelFactory] with the factory
     * that provides a view model with mocked use cases
     */
    var onFragmentInject: ((Fragment) -> Unit)? = null

    /**
     * Wrap [AndroidInjection.inject] method with helpful utilities for better testability of [Activity] class.
     *
     * Use] [onActivityInject] to customize behavior of this method in tests.
     */
    fun inject(activity: Activity) {
        onActivityInject?.invoke(activity)
        onActivityInject = null // Remove reference to the previous test since this will be executed only once
    }

    /**
     * Wrap [AndroidInjection.inject] method with helpful utilities for better testability of [Fragment] class.
     *
     * Use [onFragmentInject] to customize behavior of this method in tests.
     */
    fun inject(fragment: Fragment) {
        onFragmentInject?.invoke(fragment)
        onFragmentInject = null // Remove reference to the previous test since this will be executed only once
    }
}
