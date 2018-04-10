package com.thefuntasty.mvvmsample.ui.detail

import com.thefuntasty.mvvm.BaseViewModel
import com.thefuntasty.mvvm.livedata.DefaultValueLiveData
import javax.inject.Inject

class DetailViewModel @Inject constructor(): BaseViewModel() {

    val stringNumber = DefaultValueLiveData("0")
    private val number = DefaultValueLiveData(0)

    override fun onStart() {
        number.observeWithoutOwner { stringNumber.value = it.toString() }
    }

    fun incrementNumber() {
        number.value = number.value() + 1
    }
}
