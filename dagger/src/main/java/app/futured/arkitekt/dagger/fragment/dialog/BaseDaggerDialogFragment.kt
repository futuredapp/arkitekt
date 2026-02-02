package app.futured.arkitekt.dagger.fragment.dialog

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.futured.arkitekt.core.BaseLegacyCoreViewModel
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.fragment.dialog.ViewModelDialogFragment
import app.futured.arkitekt.dagger.ViewModelCreator
import app.futured.arkitekt.dagger.inject.TestableAndroidInjection

abstract class BaseDaggerDialogFragment<VM : BaseLegacyCoreViewModel<VS>, VS : ViewState> :
    ViewModelDialogFragment<VM, VS>(), ViewModelCreator<VM> {


    override val viewModel: VM by lazy { getVM() }

    override fun onAttach(context: Context) {
        TestableAndroidInjection.inject(this)
        super.onAttach(context)
    }

    private fun getVM(): VM = ViewModelProvider(this, viewModelFactory).get(viewModelFactory.viewModelClass.java)
}
