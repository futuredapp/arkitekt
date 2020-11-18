package app.futured.arkitekt.dagger.fragment

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.fragment.BindingViewModelFragment
import app.futured.arkitekt.dagger.inject.TestableAndroidInjection
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

abstract class BaseDaggerBindingFragment<VM : BaseViewModel<VS>, VS : ViewState, B : ViewDataBinding> :
    BindingViewModelFragment<VM, VS, B>() {

    @Inject internal lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onAttach(context: Context) {
        TestableAndroidInjection.inject(this)
        super.onAttach(context)
    }
}
