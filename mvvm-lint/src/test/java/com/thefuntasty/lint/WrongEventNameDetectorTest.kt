package com.thefuntasty.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test

class WrongEventNameDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector {
        return WrongEventNameDetector()
    }

    override fun getIssues(): MutableList<Issue> {
        return mutableListOf(WrongEventNameDetector.ISSUE)
    }

    private val eventStub = kotlin("""
        package com.thefuntasty.mvvm.event
        
        abstract class Event<T>
        """).indented()

    @Test
    fun testBasic() {
        lint().files(
            eventStub,
            kotlin("""
                import com.thefuntasty.mvvm.event.Event
                
                sealed class MainEvent : Event<MainViewState>()
                
                object ShowDetail : MainEvent()
    
                object ShowFormEvent : MainEvent()
            """).indented()
        )
            .issues(WrongEventNameDetector.ISSUE)
            .run()
            .expectErrorCount(1)
    }
}
