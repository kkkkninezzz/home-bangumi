package top.rainine.homebangumi.api.annotation.valid;

/**
 * @author rainine
 * @description 针对于integer类型
 * @date 2023/6/25 14:34:06
 */
public class ValidEnumValueConstraintValidatorForInteger extends AbstractValidEnumValueConstraintValidator<Integer> {
    @Override
    protected Class<?> getTypeClass() {
        return Integer.class;
    }
}
