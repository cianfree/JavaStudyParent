package com.bxtpw.common.validation.validator;

import com.bxtpw.common.validation.annotations.Phone;

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
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || "".equals(value.trim()))
            return true;
        if (value.matches("((\\+86)|(86))?1[3|4|5|8]\\d{9}")) {
            return true;
        }
        return false;
    }
}
