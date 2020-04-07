package ${packageName}
<#if useCaseType=="coroutines">
import app.futured.arkitekt.crusecases.BaseCrViewModel
<#else>
import app.futured.arkitekt.rxusecases.BaseRxViewModel
</#if>
import javax.inject.Inject

class ${className}ViewModel @Inject constructor(
	override val viewState: ${className}ViewState
<#if useCaseType=="coroutines">
) : BaseCrViewModel<${className}ViewState>()
<#else>
) : BaseRxViewModel<${className}ViewState>()
</#if>

