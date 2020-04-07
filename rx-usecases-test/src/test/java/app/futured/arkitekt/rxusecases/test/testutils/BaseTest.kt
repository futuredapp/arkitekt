package app.futured.arkitekt.rxusecases.test.testutils

import io.github.plastix.rxschedulerrule.RxSchedulerRule
import org.junit.Rule

abstract class BaseTest {

    @get:Rule var rxJavaRule = RxSchedulerRule()
}
