package com.thefuntasty.bindingadapters

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

/**
 *  Set drawable from resourceId
 *  @param resource drawable resource id
 */
@BindingAdapter("app:srcCompat")
fun ImageView.bindSrcCompat(@DrawableRes resource: Int) {
    setImageResource(resource)
}
