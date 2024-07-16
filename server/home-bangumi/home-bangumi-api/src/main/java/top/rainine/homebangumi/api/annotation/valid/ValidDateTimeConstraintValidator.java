package top.rainine.homebangumi.api.annotation.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author rainine
 * @description 验证日期格式
 * @date 2023/6/25 14:13:44
 */
@Slf4j
public class ValidDateTimeConstraintValidator implements ConstraintValidator<ValidDateTime, String> {

    private ValidDateTime annotation;

    private String pattern;

    @Override
    public void initialize(ValidDateTime constraintAnnotation) {
        this.annotation = constraintAnnotation;
        this.pattern = this.annotation.pattern();

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果value为null，那么就认为不进行后续验证
        if (Objects.isNull(value)) {
            return true;
        }

        try {
            LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
