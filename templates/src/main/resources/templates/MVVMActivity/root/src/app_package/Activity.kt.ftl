package ${packageName}

import android.content.Context
import android.content.Intent
import ${applicationPackage}.R
import ${applicationPackage}.databinding.Activity${className}Binding
import ${applicationPackage}.ui.base.BaseBindingActivity
import javax.inject.Inject

class ${className}Activity : BaseBindingActivity<${className}ViewModel, ${className}ViewState, Activity${className}Binding>(), ${className}View {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, ${className}Activity::class.java)
    }

    @Inject override lateinit var viewModelFactory: ${className}ViewModelFactory

    override val layoutResId = R.layout.${activityToLayout(className)}
}
