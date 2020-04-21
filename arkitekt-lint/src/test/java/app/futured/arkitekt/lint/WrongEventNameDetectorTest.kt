package app.futured.arkitekt.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test

class WrongEventNameDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector {
        return WrongEventNameDetector()
    }

    override fun getIssues(): MutableList<Issue> {
        return mutableListOf(WrongEventNameDetector.ISSUE_MUSSING_SUFFIX, WrongEventNameDetector.ISSUE_MISSPELL)
    }

    private val eventStub = kotlin("""
        package app.futured.arkitekt.core.event
        
        abstract class Event<T : ViewState>
        """).indented()

    @Test
    fun testMissingSuffixWarning() {
        lint()
            .files(
                eventStub,
                kotlin("""
                import app.futured.arkitekt.core.event.Event
                
                sealed class MainEvent : Event<MainViewState>()
                
                object ShowDetailEvent : MainEvent()
                
                object ShowDetail : MainEvent()
    
                object ShowFormEvent : MainEvent()
                
                object ShowForm : MainEvent()
            """).indented()
            )
            .issues(WrongEventNameDetector.ISSUE_MUSSING_SUFFIX)
            .run()
            .expectWarningCount(2)
    }

    @Test
    fun testMisspellWarning() {
        lint()
            .files(
                eventStub,
                kotlin("""
                import app.futured.arkitekt.core.event.Event
                
                sealed class MainEvent : Event<MainViewState>()
                
                object ShowDetailEvent : MainEvent()
                
                object ShowDetailEventt : MainEvent()
                
                object ShowFormEvent : MainEvent()
                
                object ShowFormEvents : MainEvent()
                
                object ShowEventDataFormEvents : MainEvent()
            """).indented()
            )
            .issues(WrongEventNameDetector.ISSUE_MISSPELL)
            .run()
            .expectWarningCount(3)
    }

    @Test
    fun testNoWarnings() {
        lint()
            .files(
                eventStub,
                kotlin("""
                import app.futured.arkitekt.core.event.Event
                
                sealed class MainEvent : Event<MainViewState>()
                
                object ShowDetailEvent : MainEvent()
    
                object ShowFormEvent : MainEvent()
                
                object SendEventDataEvent : MainEvent()
            """).indented()
            )
            .issues(
                WrongEventNameDetector.ISSUE_MUSSING_SUFFIX,
                WrongEventNameDetector.ISSUE_MISSPELL
            )
            .run()
            .expectClean()
    }
}
