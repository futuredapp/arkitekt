plugins {
    id("kotlin")
}

dependencies {

    compileOnly(kotlin(Deps.Kotlin.stdlib, org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))
    compileOnly(Deps.Lint.api)
    compileOnly(Deps.Lint.checks)

    testImplementation(Deps.Lint.core)
    testImplementation(Deps.Lint.tests)
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Lint-Registry-v2"] = "app.futured.arkitekt.lint.MvvmIssueRegistry"
    }
}
