package com.hundsun.boss.common.beanvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.hundsun.boss.common.utils.CommonUtil;

public class NumbericValidator implements ConstraintValidator<Numberic, String> {

    public void initialize(Numberic constraintAnnotation) {
    }

    public boolean isValid(String value, ConstraintValidatorContext arg1) {
        if (CommonUtil.isNullorEmpty(value))
            return true;
        try {
            Double.parseDouble(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}