package app.futured.arkitekt.examplehilt.ui.first

import androidx.navigation.NavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.futured.arkitekt.crusecases.test.mockExecute
import app.futured.arkitekt.examplehilt.domain.GetRandomNumberUseCase
import app.futured.arkitekt.sample.hilt.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.mockk
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import androidx.test.filters.MediumTest
import app.futured.arkitekt.examplehilt.tools.launchFragmentInHiltContainer
import app.futured.arkitekt.examplehilt.tools.launchFragmentInHiltContainerNavigations
import org.junit.Before

@MediumTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class FirstFragmentTest {

    private val mockGetRandomNumberUseCase: GetRandomNumberUseCase = mockk()

    @Inject lateinit var getRandomNumberUseCase: GetRandomNumberUseCase

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        // Populate Inject fields in test class
        hiltRule.inject()
    }

    @Test
    fun testRandomNumberUsedInLayout() {
        mockGetRandomNumberUseCase.mockExecute { 15 }

        // FragmentScenario cannot be used with Hilt - thus the workaround is necessary
        launchFragmentInHiltContainer<FirstFragment> {

        }

        // TODO: testing support for navigation components - under construction
        /*launchFragmentInHiltContainerNavigations<FirstFragment>(
            NavHostController(ApplicationProvider.getApplicationContext()),
            R.id.nav_host_fragment,
            R.navigation.nav_graph
        )*/

        onView(withId(R.id.random_number))
            .check(matches(withText(Matchers.containsString("Random number: 15"))))
    }
}
