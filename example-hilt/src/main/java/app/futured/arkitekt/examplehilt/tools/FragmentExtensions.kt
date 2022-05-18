package app.futured.arkitekt.examplehilt.tools

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import app.futured.arkitekt.core.fragment.BindingViewModelFragment

fun Fragment.navigateBack() {
    hideKeyboard()
    findNavController().navigateUp()
}

fun Fragment.navigateBack(@IdRes destinationId: Int, inclusive: Boolean = false) {
    hideKeyboard()
    findNavController()
        .popBackStack(destinationId, inclusive)
}

fun Fragment.navigateTo(navDirections: NavDirections, options: NavOptions? = null) = try {
    hideKeyboard()
    findNavController().navigate(navDirections, options)
} catch (e: Exception) {
    Log.e(this::class.java.simpleName, "navigateTo", e)
}

fun Fragment.navigateTo(navDirections: NavDirections, navigatorExtras: Navigator.Extras) = try {
    hideKeyboard()
    findNavController().navigate(navDirections, navigatorExtras)
} catch (e: Exception) {
    Log.e(this::class.java.simpleName, "navigateTo", e)
}

fun Fragment.hideKeyboard() {
    if (this is BindingViewModelFragment<*, *, *>) {
        hideKeyboard()
    }
}

private fun BindingViewModelFragment<*, *, *>.hideKeyboard() {
    binding.root.apply {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(windowToken, 0)
        clearFocus()
    }
}
