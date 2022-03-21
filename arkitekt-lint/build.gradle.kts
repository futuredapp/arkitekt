plugins {
    id("kotlin")
}

dependencies {

    compileOnly(Deps.Lint.api)
    compileOnly(Deps.Lint.checks)
    compileOnly(kotlin(Deps.Kotlin.stdlib, org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))

    testImplementation(Deps.Lint.core)
    testImplementation(Deps.Lint.tests)
    testImplementation(Deps.Test.jUnit)
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Lint-Registry-v2"] = "app.futured.arkitekt.lint.MvvmIssueRegistry"
    }
}
