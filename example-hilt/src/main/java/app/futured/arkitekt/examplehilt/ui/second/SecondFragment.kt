package app.futured.arkitekt.examplehilt.ui.second

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import app.futured.arkitekt.examplehilt.ui.NavigationViewModel
import app.futured.arkitekt.examplehilt.ui.base.BaseHiltFragment
import app.futured.arkitekt.sample.hilt.R
import app.futured.arkitekt.sample.hilt.databinding.FragmentSecondBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : BaseHiltFragment<SecondViewModel, SecondViewState, FragmentSecondBinding>(), SecondView {

    override val viewModel: SecondViewModel by viewModels()

    val navViewModel: NavigationViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override val layoutResId = R.layout.fragment_second

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.navViewModel = navViewModel
        navViewModel.outputMessage.observe(viewLifecycleOwner) { Log.d("SecondFragment", it) }
    }
}
