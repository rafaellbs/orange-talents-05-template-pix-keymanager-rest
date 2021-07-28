package br.com.zupedu.pix.validacoescustomizadas

import br.com.zupedu.pix.cadastrochave.NovaChavePixRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidarChavePixValidator::class])
annotation class ValidarChavePix(
    val message: String = "Chave pix inválida",
    val groups: Array<KClass<Any>> =[],
    val payload:Array<KClass<Payload>> = []
)

@Singleton
class ValidarChavePixValidator: ConstraintValidator<ValidarChavePix, NovaChavePixRequest> {
    override fun isValid(
        value: NovaChavePixRequest?,
        annotationMetadata: AnnotationValue<ValidarChavePix>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value?.tipo == null) {
            return false
        }

        return value.tipo.valida(value?.chave)
    }

}