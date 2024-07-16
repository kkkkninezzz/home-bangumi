package top.rainine.homebangumi.api.annotation.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 支持的类型:
 * * Integer
 * * Long
 * * String
 *
 * @author rainine
 * @description 验证枚举类的值是否有效
 * @date 2023/6/20 14:58:46
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = {
        ValidEnumValueConstraintValidatorForInteger.class,
        ValidEnumValueConstraintValidatorForLong.class,
        ValidEnumValueConstraintValidatorForString.class,
})
public @interface ValidEnumValue {

    String message() default "enum value is invalid";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * 要验证的枚举类
     * 枚举类必须提供 public static boolean contains(T value)的方法
     * 方法签名中的入参类型需要符合字段的类型
     *
     * private Integer code;
     * 那么对应的枚举类，最好提供public static boolean contains(Integer code)的方法
     * */
    Class<?> enumClass();

    /**
     * 枚举类提供用于检查的枚举值是否存在的方法名
     * 如果不提供，那么默认使用 contains()
     * */
    String checkMethodName() default "";

    /**
     * 枚举类提供用于检查的枚举值是否存在的方法参数类型
     * 如果不提供，那么默认使用 DefaultMethodParamType.class
     * 在校验时，以当前字段的类型作为 checkMethod 的参数类型
     * */
    Class<?> checkMethodParamType() default DefaultMethodParamType.class;
}
















