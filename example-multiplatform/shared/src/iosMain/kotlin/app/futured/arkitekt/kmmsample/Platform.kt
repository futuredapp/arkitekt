package app.futured.arkitekt.kmmsample

import com.rudolfhladik.arkitektexample.db.CommonDatabase
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import platform.UIKit.UIDevice

actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun createDb(): CommonDatabase {
    val driver = NativeSqliteDriver(CommonDatabase.Schema, "commondb.db")
    return CommonDatabase(driver)
}
