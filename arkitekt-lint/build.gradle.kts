plugins {
    id("kotlin")
}

dependencies {
    compileOnly(Deps.Lint.api)
    compileOnly(Deps.Lint.checks)

    testImplementation(Deps.Lint.core)
    testImplementation(Deps.Lint.tests)
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Lint-Registry-v2"] = "com.thefuntasty.lint.MvvmIssueRegistry"
    }
}
