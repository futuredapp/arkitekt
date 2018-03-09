package com.thefuntasty.mvvm

import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlin.reflect.KClass

abstract class BaseViewModelActivity<VM : BaseViewModel, B : ViewDataBinding> :
        DaggerAppCompatActivity(), BaseView {

    lateinit var viewModel: VM
    lateinit var binding: B

    abstract fun createViewModel(): VM

    abstract fun inflateBindingLayout(layoutInflater: LayoutInflater): B?

    fun getViewModelFromProvider(factory: BaseViewModelFactory<VM>, viewModelKClass: KClass<VM>): VM {
        return ViewModelProviders.of(this, factory).get(viewModelKClass.java)
    }
}
