package com.thefuntasty.mvvmsample.ui.form

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.thefuntasty.mvvm.crusecases.test.mockExecute
import app.futured.arkitekt.core.view.asProvider
import app.futured.arkitekt.core.view.doAfterActivityInjection
import com.thefuntasty.mvvmsample.R
import com.thefuntasty.mvvmsample.domain.ObserveFormUseCase
import com.thefuntasty.mvvmsample.domain.SaveFormUseCase
import com.thefuntasty.mvvmsample.tools.ToastCreator
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FormActivityTest {

    private val mockSaveFormUseCase: SaveFormUseCase = mockk()
    private val mockObserveFormUseCase: ObserveFormUseCase = mockk()
    private val mockToastCreator: ToastCreator = mockk(relaxUnitFun = true)

    @Before
    fun setUp() {
        doAfterActivityInjection<FormActivity> { activity ->
            val viewModelProvider = FormViewModel(mockSaveFormUseCase, mockObserveFormUseCase, FormViewState()).asProvider()
            activity.viewModelFactory = FormViewModelFactory(viewModelProvider)
            activity.toastCreator = mockToastCreator
        }

        // Mock methods that are called during initialization with no-op responses
        mockObserveFormUseCase.mockExecute { flowOf() }
    }

    @Test
    fun whenNameAndPasswordIsSubmitted_andSaveUseCaseIsSuccessful_thenToastIsShown() {
        // GIVEN
        val args = SaveFormUseCase.Data("name" to "password")
        mockSaveFormUseCase.mockExecute(args) { "name" to "password" }

        // WHEN
        launchActivity<FormActivity>()
        onView(withId(R.id.form_login)).perform(typeText("name"))
        onView(withId(R.id.form_password)).perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.form_submit)).perform(click())

        // THEN
        verify { mockToastCreator.showToast(any(), "name password") }
    }

    @Test
    fun whenActivityIsStarted_thenFormIsObservedAndSavedToStoredContent() {
        // GIVEN
        mockObserveFormUseCase.mockExecute { flowOf("new" to "value") }

        // WHEN
        launchActivity<FormActivity>()

        // THEN
        onView(withId(R.id.form_storedContent)).check(matches(withText(containsString("new value"))))
    }
}
