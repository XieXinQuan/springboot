package com.huanting.quan.annotation;


import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/22
 *
 * 现已废弃,已存在javax.validation.constraints.Email
 */
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface email {
    String message() default "邮箱格式不正确";

    Class<?>[] group() default {};

    Class<? extends Payload>[] payload() default {};
}
