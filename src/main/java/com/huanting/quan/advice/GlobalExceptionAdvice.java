package com.huanting.quan.advice;

import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.exception.GlobalException;
import com.huanting.quan.util.ResultUtil;
import com.huanting.quan.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/11
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception e){
        String name = e.getClass().getName();
        logger.error("There is a serious system exception, please deal with it...");
        logger.error("Exception Start...");
        logger.error("Exception Type : {}", e.getClass().getName());
        logger.error("Exception Case: {}" , e.getMessage());
        logger.error("Exception End...");
        return ResultUtil.SystemException();
    }

    @ExceptionHandler(value = GlobalException.class)
    public String exceptionHandler(GlobalException e){
        Integer status = e.getStatus() == null ? ResultEnum.CustomException.getKey() : e.getStatus();
        String msg = e.getMsg();
        if(msg == null){
            for(ResultEnum re : ResultEnum.values()){
                if(re.getKey().equals(status)) {
                    msg = re.getValue();
                    break;
                }
            }
        }
        logger.info("Custom Exception Start");
        logger.info(ResultUtil.CustomException(status, msg));
        logger.info("Custom Exception End");
        return ResultUtil.CustomException(status, msg);
    }

    @ExceptionHandler(value = ValidationException.class)
    public String exceptionHandler(ValidationException e){
        logger.info("Valid Exception Start...");
        String msg = StringUtils.validExceptionStr(e.getMessage());
        logger.info(msg);
        logger.info("Valid Exception End...");
        return ResultUtil.CustomException(msg);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public String exceptionHandler(ConstraintViolationException e){
        logger.info("Valid Exception Start...");
        String msg = StringUtils.validExceptionStr(e.getMessage());
        logger.info(msg);
        logger.info("Valid Exception End...");
        return ResultUtil.CustomException(msg);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public String exceptionHandler(MissingServletRequestParameterException e){
        logger.info("MissingServletRequestParameter Exception Start...");
        logger.info("Please Check Parameter Is Not Null, Thanks. ");
        logger.info("MissingServletRequestParameter Exception  End...");

        return ResultUtil.CustomException(ResultEnum.PasswordIsError.getKey(), "Please Check Parameter Is Not Null, Thanks.");
    }

    @ExceptionHandler(value = JpaSystemException.class)
    public String exceptionHandler(JpaSystemException e){
        logger.info("JpaSystem Exception Start...");
        logger.info("Exception Case : {}" , e.getMessage());
        logger.info("JpaSystem Exception  End...");

        return ResultUtil.CustomException(ResultEnum.PasswordIsError.getKey(), "Dd Error...");
    }

}
