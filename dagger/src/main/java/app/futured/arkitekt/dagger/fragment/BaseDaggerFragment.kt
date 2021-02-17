package app.futured.arkitekt.dagger.fragment

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import app.futured.arkitekt.core.BaseViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.fragment.ViewModelFragment
import app.futured.arkitekt.dagger.ViewModelCreator
import app.futured.arkitekt.dagger.inject.TestableAndroidInjection

abstract class BaseDaggerFragment<VM : BaseViewModel<VS>, VS : ViewState> :
    ViewModelFragment<VM, VS>(), ViewModelCreator<VM> {

    override val viewModel: VM by lazy { getVM() }

    override fun onAttach(context: Context) {
        TestableAndroidInjection.inject(this)
        super.onAttach(context)
    }

    private fun getVM(): VM = ViewModelProvider(this, viewModelFactory).get(viewModelFactory.viewModelClass.java)
}
