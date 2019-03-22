package com.thefuntasty.extensions

import android.view.View

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
