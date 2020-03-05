package com.huanting.quan.aspect;

import com.huanting.quan.util.ResultUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/11
 */
@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
    切入点描述 这个是controller包的切入点
     */
    @Pointcut("execution(public * com.huanting.quan.controller..*.*(..))")
    public void controllerLog(){}//签名，可以理解成这个切入点的一个名称

    @Before("controllerLog()") //在切入点的方法run之前要干的
    public void logBeforeController(JoinPoint joinPoint) {
        //这个RequestContextHolder是Springmvc提供来获得请求的东西
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();

        // 记录下请求内容
        logger.info("Request URL : " + request.getRequestURL().toString());
        logger.info("Request Method: " + request.getMethod());
        logger.info("Request IP : " + request.getRemoteAddr());
        logger.info("Request Parameter" + Arrays.toString(joinPoint.getArgs()));

        //下面这个getSignature().getDeclaringTypeName()是获取包+类名的   然后后面的joinPoint.getSignature.getName()获取了方法名
        logger.info("Request Class " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }
    @AfterReturning(returning = "returnOb", pointcut = "controllerLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object returnOb) {
        logger.info("Request Success Return Data : " + Optional.ofNullable(returnOb).map(Object::toString).orElse("Null"));
    }
    @Around("controllerLog()")
    public Object Around(ProceedingJoinPoint pjp) throws Throwable {

        Object proceed = pjp.proceed();

        RestController annotation = pjp.getTarget().getClass().getAnnotation(RestController.class);
        if(annotation != null) {
            return ResultUtil.Success(proceed);
        }

        return proceed;

    }
}
