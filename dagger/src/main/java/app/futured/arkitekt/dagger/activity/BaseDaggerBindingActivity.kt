package app.futured.arkitekt.dagger.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.activity.BindingViewModelActivity
import app.futured.arkitekt.dagger.inject.TestableAndroidInjection

abstract class BaseDaggerBindingActivity<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BindingViewModelActivity<VM, VS, B>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        TestableAndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}
