package com.thefuntasty.bindingadapters

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter

/**
 *  Set onclick listener to navigation icon
 *  @param listener OnClickListener
 */
@BindingAdapter("app:navigationIconClick")
fun Toolbar.navigationIconClick(listener: View.OnClickListener) = setNavigationOnClickListener(listener)
