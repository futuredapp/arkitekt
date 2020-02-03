package com.thefuntasty.mvvmsample.tools

fun randomError() {
    check(!listOf(false, false, true).random()) { "Random exception" }
}
