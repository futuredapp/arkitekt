package com.thefuntasty.mvvm.livedata

fun <T:Any> uiData(initValue: T) = UiData(initValue)

fun <T:Any> uiData(initBlock: () -> T) = uiData(initBlock())

fun <T:Any> T.toUiData() = uiData(this)
