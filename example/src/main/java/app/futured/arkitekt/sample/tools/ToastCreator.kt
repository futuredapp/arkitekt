package app.futured.arkitekt.sample.tools

import android.content.Context
import android.widget.Toast
import javax.inject.Inject

class ToastCreator @Inject constructor() {
    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}
