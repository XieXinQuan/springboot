package com.huanting.quan.service;

import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.entity.User;
import com.huanting.quan.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/27
 */
public class BaseService {
    @Autowired
    HttpServletRequest request;

    public Long getCurrentUserId(){
        if (checkSessionOutDue()) {
            return null;
        }
        return Optional.ofNullable(getCurrentUser()).map(User::getId).orElse(null);

    }

    public User getCurrentUser(){
        if (checkSessionOutDue()){
            return null;
        }
        return (User)request.getSession().getAttribute("User");
    }

    public boolean checkSessionOutDue(){
        return (request == null || request.getSession() == null);
    }

    public String getCurrentUserName(){
        return Optional.ofNullable(getCurrentUser()).map(User::getName).orElse("");
    }

    public HttpSession getSession(){
        if (checkSessionOutDue()) {
            throw new GlobalException(ResultEnum.GoToLogin.getKey());
        }
        return request.getSession();

    }

}
