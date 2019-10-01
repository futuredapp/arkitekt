package ${packageName}

import com.thefuntasty.mvvm.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class ${className}ViewModelFactory @Inject constructor(
	override val viewModelProvider: Provider<${className}ViewModel>
) : BaseViewModelFactory<${className}ViewModel>() {
	override val viewModelClass: KClass<${className}ViewModel> = ${className}ViewModel::class
}
