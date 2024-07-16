package top.rainine.homebangumi.core.runtimehints;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import top.rainine.homebangumi.api.annotation.valid.*;
/**
 * @authoer rainine
 * @date 2024/7/11 23:29
 * @desc
 */
@ImportRuntimeHints(CustomValidRuntimeHints.CustomValidRegistrar.class)
@Configuration
public class CustomValidRuntimeHints {
    static class CustomValidRegistrar implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(ValidEnumValueConstraintValidatorForInteger.class, MemberCategory.values());
            hints.reflection().registerType(ValidEnumValueConstraintValidatorForLong.class, MemberCategory.values());
            hints.reflection().registerType(ValidEnumValueConstraintValidatorForString.class, MemberCategory.values());
            hints.reflection().registerType(ValidDateConstraintValidator.class, MemberCategory.values());
            hints.reflection().registerType(ValidDateTimeConstraintValidator.class, MemberCategory.values());
        }
    }
}
