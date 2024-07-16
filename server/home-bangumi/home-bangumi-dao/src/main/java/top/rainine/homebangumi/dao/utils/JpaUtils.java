package top.rainine.homebangumi.dao.utils;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Optional;

/**
 * @authoer rainine
 * @date 2024/5/18 15:45
 * @desc
 */
public class JpaUtils {
    private JpaUtils() {}


    public static BooleanExpression and(BooleanExpression where, BooleanExpression booleanExpression) {
        return Optional.ofNullable(where).map(w -> w.and(booleanExpression)).orElse(booleanExpression);
    }

    public static BooleanExpression or(BooleanExpression where, BooleanExpression booleanExpression) {
        return Optional.ofNullable(where).map(w -> w.or(booleanExpression)).orElse(booleanExpression);
    }
}
