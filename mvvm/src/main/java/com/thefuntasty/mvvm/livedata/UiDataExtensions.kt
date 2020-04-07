package com.thefuntasty.mvvm.livedata

/**
 *  Initialization function for UiData
 *  Usage:
 *  val myData: UiData<Int> = uiData(1)
 */
inline fun <reified T : Any> uiData(initValue: T) = UiData(initValue)

/**
 *  Initialization function for UiData
 *  Usage:
 *  val myData: UiData<Int> = uiData { 1 }
 */
inline fun <reified T : Any> uiData(initBlock: () -> T) = uiData(initBlock())

/**
 *  Initialization extension function for UiData
 *  Usage:
 *  val myData: UiData<Int> = 1.toUiData()
 */
inline fun <reified T : Any> T.toUiData() = uiData(this)
