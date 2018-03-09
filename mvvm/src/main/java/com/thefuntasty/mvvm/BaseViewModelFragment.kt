package com.thefuntasty.mvvm

import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import dagger.android.support.DaggerFragment
import kotlin.reflect.KClass
import kotlin.text.Typography.dagger

abstract class BaseViewModelFragment<VM : BaseViewModel, B : ViewDataBinding> :
        DaggerFragment(), BaseView {

    protected lateinit var viewModel: VM
    protected lateinit var binding: B

    abstract fun createViewModel(): VM

    abstract fun inflateBindingLayout(layoutInflater: LayoutInflater): B?

    fun getViewModelFromProvider(factory: BaseViewModelFactory<VM>, viewModelKClass: KClass<VM>): VM {
        return ViewModelProviders.of(this, factory).get(viewModelKClass.java)
    }
}
