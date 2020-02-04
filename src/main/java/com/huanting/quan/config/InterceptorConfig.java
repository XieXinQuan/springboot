package com.huanting.quan.config;

import com.huanting.quan.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/30
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器 //拦截所有请求 //对应的不拦截的请求
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login/**", "/user/allUser", "/user/test", "/",
                        "/static/**", "/alipay", "/index.html", "/favicon.ico",
                        "/error", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**",
                        "/v2/**");

        super.addInterceptors(registry);
    }
}
