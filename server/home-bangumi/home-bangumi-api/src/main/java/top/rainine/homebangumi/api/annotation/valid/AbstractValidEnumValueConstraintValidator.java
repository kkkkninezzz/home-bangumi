package top.rainine.homebangumi.api.annotation.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author rainine
 * @description 验证枚举值的抽象基类
 * @date 2023/6/25 14:13:44
 */
@Slf4j
public abstract class AbstractValidEnumValueConstraintValidator<T> implements ConstraintValidator<ValidEnumValue, T> {

    private ValidEnumValue annotation;

    /**
     * 判断枚举值是否存在的方法
     * */
    private Method containsMethod;

    /**
     * 枚举类
     * */
    private Class<?> enumClass;

    /**
     * 获取绑定的类型
     * */
    protected abstract Class<?> getTypeClass();

    @Override
    public void initialize(ValidEnumValue constraintAnnotation) {
        this.annotation = constraintAnnotation;
        enumClass = annotation.enumClass();

        String methodName = annotation.checkMethodName();
        if (Objects.isNull(methodName) || methodName.isEmpty()) {
            methodName = "contains";
        }

        Class<?> methodParamType = annotation.checkMethodParamType();
        if (Objects.equals(DefaultMethodParamType.class, methodParamType)) {
            methodParamType = getTypeClass();
        }

        try {
            containsMethod = enumClass.getDeclaredMethod(methodName, methodParamType);
        } catch (NoSuchMethodException e) {
            log.error("[validEnum]enumClass:{} not exists {} method, methodParamType: {}", enumClass, methodName, methodParamType, e);
            throw new RuntimeException(e);
        }

        if (!Objects.equals(Boolean.class, containsMethod.getReturnType())
                && !Objects.equals(boolean.class, containsMethod.getReturnType())) {
            log.error("[validEnum]enumClass:{}, method return type:{}, {} method return type is not Boolean or boolean",
                    enumClass, methodName, containsMethod.getReturnType());
            throw new RuntimeException("return type is invalid");
        }
    }

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
        // 如果没有指定值，那么也返回true
        if (Objects.isNull(value)) {
            return true;
        }


        try {
            Object returnValue = containsMethod.invoke(null, value);
            if (Objects.isNull(returnValue)) {
                return false;
            }

            return (boolean) returnValue;
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("[validEnum]invoke contains method failed, enum class:{}, method: {}, value: {}",
                    enumClass.getName(), containsMethod.getName(), value);
            return false;
        }
    }
}
