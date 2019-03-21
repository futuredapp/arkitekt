package com.thefuntasty.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 *  Set integer as text to TextView
 *  @param text integer to display
 */
@BindingAdapter("app:textInt")
fun TextView.textFromInt(text: Int) {
    this.text = text.toString()
}

/**
 *  Set spannable text to TextView
 *  @param spannableText text to set
 */
@BindingAdapter("app:spannableText")
fun TextView.setSpannableText(spannableText: CharSequence) {
    this.setText(spannableText, TextView.BufferType.SPANNABLE)
}
