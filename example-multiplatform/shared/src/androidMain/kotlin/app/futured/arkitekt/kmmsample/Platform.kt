package app.futured.arkitekt.kmmsample

import android.content.Context
import com.rudolfhladik.arkitektexample.db.CommonDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

lateinit var appContext: Context

actual fun createDb(): CommonDatabase {
    val driver = AndroidSqliteDriver(CommonDatabase.Schema, appContext, "commondb.db")
    return CommonDatabase(driver)
}
