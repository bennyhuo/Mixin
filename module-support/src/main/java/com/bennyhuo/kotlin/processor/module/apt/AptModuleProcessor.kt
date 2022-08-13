package com.bennyhuo.kotlin.processor.module.apt

import com.bennyhuo.kotlin.processor.module.utils.OPTION_KEY_LIBRARY
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * Created by benny.
 */
abstract class AptModuleProcessor : AbstractProcessor() {

    lateinit var env: ProcessingEnvironment

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        this.env = processingEnv
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val elementsByAnnotation = annotations.associate {
            it.qualifiedName.toString() to roundEnv.getElementsAnnotatedWith(it)
        }

        val isMainModule = env.options[OPTION_KEY_LIBRARY].toBoolean()
        if (isMainModule) {
            val elementsFromLibrary = AptIndexLoader(env, supportedAnnotationTypes).load()
            processMain(roundEnv, elementsByAnnotation.mapValues {
                it.value + elementsFromLibrary.getOrDefault(it.key, emptySet())
            })
        } else {
            processLibrary(roundEnv, elementsByAnnotation)
        }

        return false
    }


    abstract fun processMain(roundEnv: RoundEnvironment, elementsByAnnotation: Map<String, Set<Element>>): Set<Element>

    open fun processLibrary(roundEnv: RoundEnvironment, elementsByAnnotation: Map<String, Set<Element>>): Set<Element> {
        AptIndexGenerator(env).generate(elementsByAnnotation.values.flatten())
        return emptySet()
    }

}