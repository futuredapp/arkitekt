package com.thefuntasty.bindingadapters

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion

/**
 *  Convert true/false to View.VISIBLE and View.GONE
 *  @param visibility boolean to convert
 */
@BindingConversion
fun visibility(visibility: Boolean) = if (visibility) View.VISIBLE else View.GONE

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible(visibility: Boolean) {
    this.visibility = if (visibility) View.VISIBLE else View.GONE
}

fun View.animateShow(endAction: () -> Unit = {}) {
    visible()
    alpha = 0f
    animate().alpha(1f)
        .withEndAction { endAction() }
        .duration = 200
}

fun View.animateGone(endAction: () -> Unit = {}) {
    alpha = 1f
    animate().alpha(0f)
        .withEndAction {
            gone()
            endAction()
        }
        .duration = 200
}

fun View.animateInvisible(endAction: () -> Unit = {}) {
    alpha = 1f
    animate().alpha(0f)
        .withEndAction {
            invisible()
            endAction()
        }
        .duration = 200
}

/**
 *  Animate view visibility true/false to View.VISIBLE and View.GONE
 *  @param visibility final visible state
 */
@BindingAdapter("app:animatedVisibility")
fun View.animatedVisibility(visibility: Boolean) {
    if (visibility) {
        animateShow()
    } else {
        animateGone()
    }
}

/**
 *  Animate view visibility true/false to View.VISIBLE and View.INVISIBLE
 *  @param visibility final visible state
 */
@BindingAdapter("app:animatedVisibilityAlpha")
fun View.animatedVisibilityAlpha(visibility: Boolean) {
    if (visibility) {
        animateShow()
    } else {
        animateInvisible()
    }
}

@BindingAdapter("app:background")
fun View.setBackground(@DrawableRes resId: Int) {
    ContextCompat.getDrawable(context, resId)?.also {
        this.background = it
    }
}

/**
 *  Animate android:layout_marginStart from original value to new value specified in pixels
 *  @param pixels final margin
 *  @param duration animation duration
 */
@BindingAdapter("app:animateMarginStart", "app:marginAnimDuration", requireAll = false)
fun View.animatedMarginStart(pixels: Int, duration: Int? = null) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    ValueAnimator.ofInt(params.marginStart, pixels).apply {
        duration?.let { setDuration(it.toLong()) }
        interpolator = DecelerateInterpolator()
        addUpdateListener {
            val animatedValue = it.animatedValue as Int
            params.marginStart = animatedValue
            layoutParams = params
        }

        start()
    }
}

/**
 *  Animate android:layout_marginEnd from original value to new value specified in pixels
 *  @param pixels final margin
 *  @param duration animation duration
 */
@BindingAdapter("app:animateMarginEnd", "app:marginAnimDuration", requireAll = false)
fun View.animatedMarginEnd(pixels: Int, duration: Int? = null) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    ValueAnimator.ofInt(params.marginEnd, pixels).apply {
        duration?.let { setDuration(it.toLong()) }
        interpolator = DecelerateInterpolator()
        addUpdateListener {
            val animatedValue = it.animatedValue as Int
            params.marginEnd = animatedValue
            layoutParams = params
        }

        start()
    }
}

/**
 *  Animate view background color with TransitionDrawable
 *  @param drawable TransitionDrawable
 *  @param duration animation duration
 */
@BindingAdapter("app:animBgTransition", "app:animBgDirectedDuration", requireAll = false)
fun View.animatedColor(drawable: Drawable, duration: Int? = null) {
    val transition = drawable as TransitionDrawable
    background = transition
    when (duration) {
        null, 0 -> {
        }
        else -> {
            transition.startTransition(duration)
        }
    }
}

/**
 *  Set onFocusChangedListener
 *  @param listener View.OnFocusChangeListener
 */
@BindingAdapter("app:onFocusChanged")
fun View.onFocusChanged(listener: View.OnFocusChangeListener) {
    onFocusChangeListener = listener
}
