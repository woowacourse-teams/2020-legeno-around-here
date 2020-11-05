package wooteco.team.ittabi.legenoaroundhere.controller.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ImageConstraintValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImagesConstraint {

    String message() default "image Validation Failed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
