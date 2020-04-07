package app.futured.arkitekt.dagger.activity

import android.os.Bundle
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.activity.ViewModelActivity
import app.futured.arkitekt.dagger.inject.TestableAndroidInjection

abstract class BaseDaggerActivity<VM : BaseViewModel<VS>, VS : ViewState> :
    ViewModelActivity<VM, VS>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        TestableAndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}
