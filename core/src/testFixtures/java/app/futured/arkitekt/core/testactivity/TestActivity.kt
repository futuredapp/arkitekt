package app.futured.arkitekt.core.testactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class TestActivity : AppCompatActivity() {

    private lateinit var viewModel: TestViewModel
    private val vmFactory = TestViewModelFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, vmFactory).get(TestViewModel::class.java)
        lifecycle.addObserver(viewModel)
    }
}
