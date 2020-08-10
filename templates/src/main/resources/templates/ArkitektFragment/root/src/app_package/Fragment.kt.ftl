package ${packageName}

import ${applicationPackage}.R
import ${applicationPackage}.databinding.Fragment${className}Binding
import ${applicationPackage}.ui.base.BaseBindingFragment
import javax.inject.Inject

class ${className}Fragment : BaseBindingFragment<${className}ViewModel, ${className}ViewState, Fragment${className}Binding>(), ${className}View {

    <#if !isNavComponents>
    companion object {
        fun newInstance(): ${className}Fragment = ${className}Fragment()
    }
    </#if>

    @Inject override lateinit var viewModelFactory: ${className}ViewModelFactory

    override val layoutResId = R.layout.fragment_${classToResource(className)}
}
