package com.thefuntasty.mvvm.viewmodel.testactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

class TestActivity : AppCompatActivity() {

    private lateinit var viewModel: TestViewModel
    private val vmFactory = TestViewModelFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, vmFactory).get(TestViewModel::class.java)
        lifecycle.addObserver(viewModel)
    }
}
