package ${packageName}
<#if interactorType=="coroutines">
import com.thefuntasty.mvvm.crinteractors.BaseCrViewModel
<#else>
import com.thefuntasty.interactors.BaseRxViewModel
</#if>
import javax.inject.Inject

class ${className}ViewModel @Inject constructor(
	override val viewState: ${className}ViewState
<#if interactorType=="coroutines">
) : BaseCrViewModel<${className}ViewState>()
<#else>
) : BaseRxViewModel<${className}ViewState>()
</#if>

