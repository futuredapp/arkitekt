package ${packageName}
<#if useCaseType=="coroutines">
import com.thefuntasty.mvvm.crusecases.BaseCrViewModel
<#else>
import com.thefuntasty.mvvm.rxusecases.BaseRxViewModel
</#if>
import javax.inject.Inject

class ${className}ViewModel @Inject constructor(
	override val viewState: ${className}ViewState
<#if useCaseType=="coroutines">
) : BaseCrViewModel<${className}ViewState>()
<#else>
) : BaseRxViewModel<${className}ViewState>()
</#if>

