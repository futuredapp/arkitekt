package ${packageName}

import com.thefuntasty.interactors.BaseRxViewModel
import javax.inject.Inject

class ${className}ViewModel @Inject constructor(
    override val viewState: ${className}ViewState
) : BaseRxViewModel<${className}ViewState>()
