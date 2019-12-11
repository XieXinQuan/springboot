package com.example.Quan.advice;

import com.example.Quan.Enum.ResultEnum;
import com.example.Quan.exception.GlobalException;
import com.example.Quan.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return ResultUtil.CustomException(status, msg);
    }
}
