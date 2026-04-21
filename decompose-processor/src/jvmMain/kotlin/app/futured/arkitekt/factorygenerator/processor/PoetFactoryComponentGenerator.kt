package app.futured.arkitekt.factorygenerator.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * `PoetFactoryComponentGenerator` is responsible for generating the factory object for a given component.
 *
 * This object utilizes KotlinPoet to generate the necessary code for creating a factory that
 * uses Koin for dependency injection. It analyzes a component's constructor parameters to
 * determine which dependencies need to be manually injected and generates a `createComponent` function
 * that retrieves these dependencies from the Koin container.
 *
 * The generated factory object implements the `KoinComponent` interface and provides a
 * `createComponent` function to instantiate the target component.
 *
 * Key functionalities:
 * 1.  **Generates Factory Objects:** Creates a factory object for each component annotated
 *     with a specific annotation (implicitly handled by the ksp processor).
 * 2.  **Handles Dependency Injection:** Uses Koin's `get()` function to retrieve necessary
 *     dependencies from the Koin container.
 * 3.  **Supports Constructor Parameters:** Inspects the component's constructor to identify
 *     parameters requiring manual injection (marked with `@InjectedParam`).
 * 4.  **Handles Special Types:** Detects and handles special types like `AppComponentContext`,
 *     `Navigation`, and `Arg` for specific component needs.
 */
object PoetFactoryComponentGenerator {

    private const val INJECTED_PARAM_ANNOTATION = "InjectedParam"

    private object Imports {
        const val KOIN_COMPONENT_PACKAGE = "org.koin.core.component"
        const val KOIN_PARAMETER_PACKAGE = "org.koin.core.parameter"
        const val KOIN_COMPONENT_CLASS_NAME = "KoinComponent"
        const val KOIN_GET_FUNCTION_NAME = "get"
        const val KOIN_PARAMETERS_OF_FUNCTION_NAME = "parametersOf"
    }

    fun generateFactory(
        factoryComponent: KSClassDeclaration,
        codeGenerator: CodeGenerator,
    ) {
        // Component name without package e.g. FirstComponent
        val baseName: String = factoryComponent.qualifiedName?.asString()?.substringAfterLast('.')
            ?: error("Unable to get base name, component qualified name: ${factoryComponent.qualifiedName?.asString()}")

        val factoryComponentPackageName = factoryComponent.packageName.asString()
        val factoryClassName = ClassName(
            packageName = factoryComponentPackageName,
            simpleNames = listOf("${baseName}Factory"),
        )

        val koinComponentClass = ClassName(
            packageName = "org.koin.core.component",
            simpleNames = listOf("KoinComponent"),
        )

        val componentTypeSpec = createComponentTypeSpec(
            factoryClassName = factoryClassName,
            koinComponentClass = koinComponentClass,
            createComponentFunction = createComponentFunction(
                baseName,
                factoryComponentPackageName,
                factoryComponent,
            ),
        )

        val fileSpec = createFileSpec(factoryClassName, componentTypeSpec)

        fileSpec.writeTo(codeGenerator, aggregating = true)
    }

    private fun createComponentTypeSpec(
        factoryClassName: ClassName,
        koinComponentClass: ClassName,
        createComponentFunction: FunSpec,
    ) = TypeSpec.objectBuilder(factoryClassName)
        .addModifiers(KModifier.INTERNAL)
        .addSuperinterface(superinterface = koinComponentClass)
        .addFunction(createComponentFunction)
        .build()

    private fun createFileSpec(factoryClassName: ClassName, componentTypeSpec: TypeSpec) =
        FileSpec.builder(factoryClassName)
            .addImport(Imports.KOIN_COMPONENT_PACKAGE, Imports.KOIN_COMPONENT_CLASS_NAME)
            .addImport(Imports.KOIN_COMPONENT_PACKAGE, Imports.KOIN_GET_FUNCTION_NAME)
            .addImport(Imports.KOIN_PARAMETER_PACKAGE, Imports.KOIN_PARAMETERS_OF_FUNCTION_NAME)
            .addType(componentTypeSpec)
            .build()

    private fun createComponentFunction(
        baseName: String,
        factoryComponentPackageName: String,
        factoryComponent: KSClassDeclaration,
    ): FunSpec {
        // All constructor parameters that are annotated with @InjectedParam
        val unInjectedConstructorParams = factoryComponent.primaryConstructor?.parameters
            ?.filter { it.annotations.any { it.shortName.asString() == INJECTED_PARAM_ANNOTATION } }
            ?: error("No @InjectedParam annotation found in $baseName's constructor")

        val argsNamesAndTypes = unInjectedConstructorParams
            .mapIndexed { index, ksValueParameter ->
                val paramName = ksValueParameter.name?.asString() ?: "param$index"
                val typeName = ksValueParameter.type.toTypeName()
                paramName to typeName
            }

        val returnType = ClassName(
            packageName = factoryComponentPackageName,
            simpleNames = listOf(baseName),
        )

        val paramNames = argsNamesAndTypes.joinToString { it.first }

        val createComponentFunSpec = FunSpec.builder("createComponent")
            .apply {
                argsNamesAndTypes.forEach { (name, type) ->
                    addParameter(name = name, type = type)
                }
            }

        return createComponentFunSpec
            .returns(returnType)
            .addStatement(
                "return get(\n" +
                    "qualifier = null,\n" +
                    "parameters = { parametersOf($paramNames) },\n" +
                    ")",
            )
            .build()
    }

    private fun List<KSValueParameter>.findTypeByName(name: String): TypeName? = this
        .find { it.containsTypeName(name) }
        ?.type?.toTypeName()

    private fun KSValueParameter.containsTypeName(name: String): Boolean =
        this.type.toTypeName().toString().contains(name)
}