package app.futured.arkitekt.kmusecases

public actual class Platform actual constructor() {
    public actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}
