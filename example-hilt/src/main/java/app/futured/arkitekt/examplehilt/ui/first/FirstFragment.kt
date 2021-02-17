package app.futured.arkitekt.examplehilt.ui.first

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import app.futured.arkitekt.examplehilt.tools.navigateTo
import app.futured.arkitekt.examplehilt.ui.NavigationViewModel
import app.futured.arkitekt.examplehilt.ui.base.BaseFragment
import app.futured.arkitekt.sample.hilt.R
import app.futured.arkitekt.sample.hilt.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : BaseFragment<FirstViewModel, FirstViewState, FragmentFirstBinding>(), FirstView {

    override val viewModel: FirstViewModel by viewModels()

    val navViewModel: NavigationViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override val layoutResId = R.layout.fragment_first

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.navViewModel = navViewModel
        observeEvents()
        navViewModel.outputMessage.observe(viewLifecycleOwner) { Log.d("FirstFragment", it) }
    }

    private fun observeEvents() {
        observeEvent(NavigateToSecondFragmentEvent::class) {
            navigateTo(FirstFragmentDirections.navigateToSecondFragment(it.number))
        }
    }
}
