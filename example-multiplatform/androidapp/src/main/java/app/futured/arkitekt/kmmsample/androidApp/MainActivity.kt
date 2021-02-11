package app.futured.arkitekt.kmmsample.androidApp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import app.futured.arkitekt.kmmsample.Greeting
import app.futured.arkitekt.kmmsample.domain.ObserveLaunchesUseCase
import app.futured.arkitekt.kmusecases.Scope
import kotlinx.coroutines.CoroutineScope

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() , Scope {
    override val coroutineScope: CoroutineScope
        get() = lifecycleScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
        val tv: TextView = findViewById(R.id.text_view)
        tv.text = greet()
//
        val observeUC = ObserveLaunchesUseCase()
        observeUC.execute(Unit) {
            onNext { Log.d("Launches: ", "${it.list}") }
            onError { Log.e("Launches: ", it.message?: "", it) }
        }
    }
}
