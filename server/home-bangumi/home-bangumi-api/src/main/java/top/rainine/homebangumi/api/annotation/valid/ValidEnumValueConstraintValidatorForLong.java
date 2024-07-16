package top.rainine.homebangumi.api.annotation.valid;

/**
 * @author rainine
 * @description 针对于Long类型
 * @date 2023/6/25 14:34:06
 */
public class ValidEnumValueConstraintValidatorForLong extends AbstractValidEnumValueConstraintValidator<Long> {
    @Override
    protected Class<?> getTypeClass() {
        return Long.class;
    }
}
