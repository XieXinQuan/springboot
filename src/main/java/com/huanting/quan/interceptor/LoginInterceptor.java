package com.huanting.quan.interceptor;

import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/30
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)   {

        if(request.getSession() == null ||
            request.getSession().getAttribute("User") == null) {

            logger.info("用户未登录, 被拦截..., Request Url: {}", request.getRequestURL());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");

            PrintWriter out= null;
            try{

                out = response.getWriter();

                out.print(ResultUtil.CustomException(ResultEnum.GoToLogin.getKey(), ResultEnum.GoToLogin.getValue()));
                out.flush();
                return false;
            }catch (IOException e){

                logger.info("Exception Information: {}." , e.getMessage());
            }finally {
                if(out != null) out.close();
            }
//            try {




//            }catch (IOException e){
//            }finally {
//                if(out != null) out.close();
//            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
