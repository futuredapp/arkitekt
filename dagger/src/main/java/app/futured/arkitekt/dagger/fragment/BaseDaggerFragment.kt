package app.futured.arkitekt.dagger.fragment

import android.content.Context
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.dagger.inject.TestableAndroidInjection
import app.futured.arkitekt.core.fragment.ViewModelFragment

abstract class BaseDaggerFragment<VM : BaseViewModel<VS>, VS : ViewState> :
    ViewModelFragment<VM, VS>() {

    override fun onAttach(context: Context) {
        TestableAndroidInjection.inject(this)
        super.onAttach(context)
    }
}
