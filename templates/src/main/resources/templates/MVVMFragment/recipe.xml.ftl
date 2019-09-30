<?xml version="1.0"?>
<recipe>

    <instantiate
    	from="src/app_package/layout.xml.ftl"
      	to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />

    <instantiate
    	from="src/app_package/Events.kt.ftl"
     	to="${escapeXmlAttribute(srcOut)}/${className}Events.kt" />

    <instantiate
    	from="src/app_package/Fragment.kt.ftl"
     	to="${escapeXmlAttribute(srcOut)}/${className}Fragment.kt" />

    <instantiate
		from="src/app_package/FragmentModule.kt.ftl"
	 	to="${escapeXmlAttribute(srcOut)}/${className}FragmentModule.kt" />

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

    <open file="${escapeXmlAttribute(srcOut)}/${className}Fragment.kt" />

</recipe>
