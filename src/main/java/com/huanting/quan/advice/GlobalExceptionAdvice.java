package com.huanting.quan.advice;

import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.exception.GlobalException;
import com.huanting.quan.util.ResultUtil;
import com.huanting.quan.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        logger.error("There is a serious system exception, please deal with it...");
        logger.error("Exception Information...");
        logger.error("Exception Start...");
        logger.error("Exception Case:"+e.getMessage());
        logger.error("Exception End...");
        return ResultUtil.SystemException(e.getMessage());
    }

    @ExceptionHandler(value = GlobalException.class)
    public String exceptionHandler(GlobalException e){
        Integer status = e.getStatus() == null ? ResultEnum.CustomException.getKey() : e.getStatus();
        String msg = e.getMsg();
        if(msg == null){
            for(ResultEnum re : ResultEnum.values()){
                if(re.getKey() == status) {
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
}
