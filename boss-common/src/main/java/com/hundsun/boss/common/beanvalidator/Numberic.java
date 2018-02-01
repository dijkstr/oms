package com.hundsun.boss.common.beanvalidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumbericValidator.class)
public @interface Numberic {

    String message() default "不是数字类型";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}