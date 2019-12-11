package com.example.Quan.util;

import com.alibaba.fastjson.JSON;
import com.example.Quan.Enum.ResultEnum;
import com.example.Quan.entity.Result;

public class ResultUtil {
    public static String Success(Object data){
        return JSON.toJSONString(new Result(ResultEnum.Success.getKey(), ResultEnum.Success.getValue(), data));
    }

//     自定义异常
    public static String CustomException(Integer status, String msg, Object data){
        return JSON.toJSONString(new Result(ResultEnum.CustomException.getKey(), ResultEnum.CustomException.getValue(), data));
    }
    public static String CustomException(Integer status, String msg){
        return ResultUtil.CustomException(ResultEnum.CustomException.getKey(), ResultEnum.CustomException.getValue(), null);
    }
    public static String CustomException(String msg){
        return ResultUtil.CustomException(ResultEnum.CustomException.getKey(), msg);
    }
    public static String CustomException(Integer status){
        return ResultUtil.CustomException(status, ResultEnum.CustomException.getValue());
    }
    public static String CustomException(){
        return ResultUtil.CustomException(ResultEnum.CustomException.getKey(), ResultEnum.CustomException.getValue());
    }

    public static String GoToLogin(Object data){
        return JSON.toJSONString(new Result(ResultEnum.GoToLogin.getKey(), ResultEnum.GoToLogin.getValue(), data));
    }

    public static String SystemException(String msg){
        return JSON.toJSONString(new Result(ResultEnum.SystemException.getKey(), msg));
    }
}
