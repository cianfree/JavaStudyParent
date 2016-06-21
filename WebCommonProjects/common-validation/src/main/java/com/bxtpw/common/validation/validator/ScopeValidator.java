package com.bxtpw.common.validation.validator;

import com.bxtpw.common.validation.annotations.Scope;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号码验证器
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/3 8:58
 * @since 0.1
 */
public class ScopeValidator implements ConstraintValidator<Scope, Number> {

    private int min;
    private int max;
    private boolean allowNull;

    @Override
    public void initialize(Scope constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (allowNull) {
            return null == value || (value.intValue() >= min && value.intValue() <= max);
        } else {
            return null != value && value.intValue() >= min && value.intValue() <= max;
        }
    }
}
