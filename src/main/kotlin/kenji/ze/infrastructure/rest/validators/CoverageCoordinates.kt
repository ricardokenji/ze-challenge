package kenji.ze.infrastructure.rest.validators

import kenji.ze.infrastructure.rest.PartnerRestController
import org.springframework.stereotype.Component
import java.lang.annotation.ElementType
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [CoverageCoordinatesConstraint::class])
@Target(allowedTargets = [FUNCTION, FIELD, ANNOTATION_CLASS, CONSTRUCTOR, VALUE_PARAMETER, TYPE_PARAMETER, PROPERTY_GETTER])
@Retention(AnnotationRetention.RUNTIME)
annotation class CoverageCoordinates(
    val message: String = "The coverage coordinates list should contain at least 4 points and must start and end at the same point.",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class CoverageCoordinatesConstraint(
    private val validator: CoverageCoordinatesValidator
): ConstraintValidator<CoverageCoordinates, List<List<List<PartnerRestController.PointDTO>>>> {

    override fun initialize(contactNumber: CoverageCoordinates) {}

    override fun isValid(values: List<List<List<PartnerRestController.PointDTO>>>, context: ConstraintValidatorContext): Boolean {
        return validator.isValid(values)
    }
}

@Component
class CoverageCoordinatesValidator {
    fun isValid(values: List<List<List<PartnerRestController.PointDTO>>>): Boolean {
        values.forEach { polygons ->
            polygons.forEach { polygon ->
                if (polygon.size != 4) {
                    return false
                }
                if (polygon.first() != polygon.last()) {
                    return false
                }
            }
        }
        return true
    }
}
