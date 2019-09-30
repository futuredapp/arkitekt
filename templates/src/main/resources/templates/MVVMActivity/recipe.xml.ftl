<?xml version="1.0"?>
<recipe>

    <instantiate
    	from="src/app_package/layout.xml.ftl"
     	to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />

    <instantiate
    	from="src/app_package/Activity.kt.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}Activity.kt" />

    <instantiate
    	from="src/app_package/ActivityModule.kt.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}ActivityModule.kt" />

    <instantiate
    	from="src/app_package/Events.kt.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}Events.kt" />

    <instantiate
    	from="src/app_package/View.kt.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}View.kt" />

    <instantiate
    	from="src/app_package/ViewModel.kt.ftl"
      	to="${escapeXmlAttribute(srcOut)}/${className}ViewModel.kt" />

    <instantiate
    	from="src/app_package/ViewModelFactory.kt.ftl"
    	to="${escapeXmlAttribute(srcOut)}/${className}ViewModelFactory.kt" />

    <instantiate
    	from="src/app_package/ViewState.kt.ftl"
      	to="${escapeXmlAttribute(srcOut)}/${className}ViewState.kt" />

    <open file="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />

    <open file="${escapeXmlAttribute(srcOut)}/${className}Activity.kt" />

</recipe>
