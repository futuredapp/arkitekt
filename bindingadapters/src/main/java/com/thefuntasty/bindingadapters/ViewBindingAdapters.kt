package com.thefuntasty.bindingadapters

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.thefuntasty.extensions.animateGone
import com.thefuntasty.extensions.animateInvisible
import com.thefuntasty.extensions.animateShow

/**
 *  Convert true/false to View.VISIBLE and View.GONE
 *  @param visibility boolean to convert
 */
@BindingConversion
fun visibility(visibility: Boolean) = if (visibility) View.VISIBLE else View.GONE

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
 *  Animate view elevation from original value to new value specified in pixels
 *  @param pixels final elevation
 *  @param duration animation duration
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@BindingAdapter("app:animElevation", "app:animElevationDuration", requireAll = false)
fun View.animateElevation(pixels: Int, duration: Int? = null) {
    ValueAnimator.ofFloat(elevation, pixels.toFloat()).apply {
        duration?.let { setDuration(it.toLong()) }
        interpolator = DecelerateInterpolator()
        addUpdateListener {
            val animatedValue = it.animatedValue as Float
            elevation = animatedValue
        }

        start()
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
