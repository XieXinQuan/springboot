package com.huanting.quan.validator;

import com.huanting.quan.annotation.email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/22
 */
public class EmailValidator implements ConstraintValidator<email, String> {
    private Pattern pattern = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");

    @Override
    public void initialize(email email){

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext){
        return pattern.matcher(value).matches();
    }
}
