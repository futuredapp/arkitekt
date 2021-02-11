object Dependencies {
    object Kotlin {
        const val version = "1.4.30"
    }

    object Ktor {
        const val version = "1.5.1" // try 1.5.0

        const val ktor = "io.ktor:ktor-client-core:$version"
        const val androidEngine = "io.ktor:ktor-client-android:$version"
        const val iOsEngine = "io.ktor:ktor-client-ios:$version"
    }

    object KotlinX {
        object Coroutines {
            const val version = "1.4.1-native-mt"
            const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        }
        object Serialization {
            const val version = "1.0.1"
            const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
        }
    }

    object Apollo {
        const val version = "2.4.5"
        const val apollo = "com.apollographql.apollo:apollo-api:$version"
    }

    object SQLDelight {
        const val version = "1.4.3"

        val runtime = "com.squareup.sqldelight:runtime:$version"
        val coroutineExtensions = "com.squareup.sqldelight:coroutines-extensions:$version"

        const val androidDriver = "com.squareup.sqldelight:android-driver:$version"
        const val nativeDriver = "com.squareup.sqldelight:native-driver:$version"
    }

    object Android {
        const val agp = "4.1.1"
    }
}
