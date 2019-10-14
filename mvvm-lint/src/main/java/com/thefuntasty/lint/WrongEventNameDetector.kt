package com.thefuntasty.lint

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

class WrongEventNameDetector : Detector(), Detector.UastScanner {

    companion object {
        val ISSUE = Issue.create(
            id = "MvvmWrongEventName",
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
    }

    override fun getApplicableUastTypes() = listOf(UClass::class.java)

    override fun applicableSuperClasses() = listOf("com.thefuntasty.mvvm.event.Event")

    override fun visitClass(context: JavaContext, declaration: UClass) {
        super.visitClass(context, declaration)

        declaration.superClass?.let { superClass ->
            if (context.evaluator.getQualifiedName(superClass) != "com.thefuntasty.mvvm.event.Event") {
                if (declaration.name?.endsWith("Event") == false) {
                    context.report(
                        issue = ISSUE,
                        scopeClass = declaration,
                        location = context.getNameLocation(declaration),
                        message = "Event names should end with 'Event' suffix. Suggested name: ${declaration.name}Event",
                        quickfixData = createQuickFix(declaration)
                    )
                }
            }
        }
    }

    private fun createQuickFix(declaration: UClass): LintFix? {
        return declaration.name?.let { name ->
            LintFix.create()
                .name("Replace with ${name}Event")
                .replace()
                .text(name)
                .with("${name}Event")
                .build()
        }
    }
}
