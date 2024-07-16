package top.rainine.homebangumi.api.annotation.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author rainine
 * @description 验证日期格式是否正确，如果字段为空，那么会验证通过
 * @date 2023/7/5 16:02:47
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = {
    ValidDateConstraintValidator.class
})
public @interface ValidDate {
    String message() default "Invalid date format";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * 有效的格式
     * */
    String pattern() default "yyyy-MM-dd";
}
