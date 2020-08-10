package app.futured.arkitekt.bindingadapters

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter

/**
 *  Set onclick listener to navigation icon
 *  @param listener OnClickListener
 */
@BindingAdapter("navigationIconClick")
fun Toolbar.navigationIconClick(listener: View.OnClickListener) = setNavigationOnClickListener(listener)

/**
 *  Set listener to menu item click.
 *  @param listener OnMenuItemClickListener
 */
@BindingAdapter("menuItemClick")
fun Toolbar.menuItemClick(listener: Toolbar.OnMenuItemClickListener) = setOnMenuItemClickListener(listener)
