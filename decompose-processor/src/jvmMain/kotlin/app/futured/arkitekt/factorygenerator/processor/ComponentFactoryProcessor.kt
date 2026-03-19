package app.futured.arkitekt.factorygenerator.processor

import app.futured.arkitekt.annotation.GenerateFactory
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.reflect.KClass

class ComponentFactoryProcessor(
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val components: Sequence<KSClassDeclaration> = resolver.findAnnotationsForClass(GenerateFactory::class)

        components.forEach { generateComponent(it) }

        return emptyList()
    }

    private fun generateComponent(component: KSClassDeclaration) =
        PoetFactoryComponentGenerator.generateFactory(component, codeGenerator)

    private fun Resolver.findAnnotationsForClass(kClass: KClass<*>): Sequence<KSClassDeclaration> =
        this.getSymbolsWithAnnotation(kClass.qualifiedName.toString())
            .filterIsInstance<KSClassDeclaration>()
}
