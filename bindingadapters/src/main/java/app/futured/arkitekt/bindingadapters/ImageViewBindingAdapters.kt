package app.futured.arkitekt.bindingadapters

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

/**
 *  Set drawable from resourceId
 *  @param resource drawable resource id
 */
@BindingAdapter("srcCompat")
fun ImageView.bindSrcCompat(@DrawableRes resource: Int) {
    setImageResource(resource)
}
