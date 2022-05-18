package app.futured.arkitekt.lint

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.uast.UClass
import java.util.EnumSet
import java.util.regex.Pattern

class WrongEventNameDetector : Detector(), Detector.UastScanner {

    companion object {
        val ISSUE_MUSSING_SUFFIX = Issue.create(
            id = "MvvmEventNameMissingSuffix",
            briefDescription = "Wrong event name",
            explanation = "Event names should end with 'Event' suffix",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                WrongEventNameDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE, Scope.TEST_SOURCES)
            )
        )

        val ISSUE_MISSPELL = Issue.create(
            id = "MvvmEventNameMisspell",
            briefDescription = "Misspelled event name",
            explanation = "Event name looks misspelled",
            category = Category.CORRECTNESS,
            priority = 3,
            severity = Severity.WARNING,
            implementation = Implementation(
                WrongEventNameDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE, Scope.TEST_SOURCES)
            )
        )

        private const val MVVM_EVENT_QUALIFIED_NAME = "app.futured.arkitekt.core.event.Event"
        private val PATTERN_MISSPELL = Pattern.compile("Event[a-z]+")
        private val PATTERN_SUFFIX = Pattern.compile("(.+Event)")
    }

    override fun getApplicableUastTypes() = listOf(UClass::class.java)

    override fun applicableSuperClasses() = listOf(MVVM_EVENT_QUALIFIED_NAME)

    override fun visitClass(context: JavaContext, declaration: UClass) {
        super.visitClass(context, declaration)

        val className = declaration.name

        val isMvvmLibraryEvent = context.evaluator.getQualifiedName(declaration) == MVVM_EVENT_QUALIFIED_NAME
        val directlyExtendsMvvmEvent = declaration.javaPsi.superClass?.let {
            context.evaluator.getQualifiedName(it)
        } == MVVM_EVENT_QUALIFIED_NAME

        val isEligibleForDetection = isMvvmLibraryEvent.not() && directlyExtendsMvvmEvent.not()

        if (className != null && isEligibleForDetection) {
            when {
                PATTERN_MISSPELL.matcher(className).find() -> {
                    val suggestedName = PATTERN_SUFFIX.matcher(className).let {
                        it.find()
                        it.group(1)
                    }

                    context.report(
                        issue = ISSUE_MISSPELL,
                        scopeClass = declaration,
                        location = context.getNameLocation(declaration),
                        message = "Event name is misspelled. Suggested name: $suggestedName",
                        quickfixData = createQuickFix(className, suggestedName)
                    )
                }

                PATTERN_SUFFIX.matcher(className).find().not() -> {
                    val suggestedName = "${declaration.name}Event"

                    context.report(
                        issue = ISSUE_MUSSING_SUFFIX,
                        scopeClass = declaration,
                        location = context.getNameLocation(declaration),
                        message = "Event names should end with 'Event' suffix. Suggested name: $suggestedName",
                        quickfixData = createQuickFix(className, suggestedName)
                    )
                }
            }
        }
    }

    private fun createQuickFix(declarationName: String, replacement: String) = LintFix.create()
        .name("Replace with $replacement")
        .replace()
        .text(declarationName)
        .with(replacement)
        .build()
}
