package app.futured.arkitekt.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class MvvmIssueRegistry : IssueRegistry() {

    override val issues: List<Issue> = listOf(
        WrongEventNameDetector.ISSUE_MUSSING_SUFFIX,
        WrongEventNameDetector.ISSUE_MISSPELL
    )

    override val api = CURRENT_API
}
