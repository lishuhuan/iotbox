package com.nbicc.gywlw.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by BigMao on 2016/11/17.
 * aop日志 for Controller.*
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* com.nbicc.gywlw.Controller.*Controller.*(..))")
    public void webLog(){}

    @Before("execution(* com.nbicc.gywlw.Controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            if(arg == null){
                break;
            }
            sb.append("arg:" + arg.toString() + " | ");
        }
        logger.info("Before Method:  请求参数为： " + sb.toString());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
//        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    @After("execution(* com.nbicc.gywlw.Controller.*Controller.*(..))")
    public void afterMethod(JoinPoint joinPoint) {
        logger.info("after method: ");
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        if(ret.toString().length() > 1000){
            logger.info("RESPONSE : " + ret.toString().substring(0,1000));
        }else{
            logger.info("RESPONSE : " + ret.toString());
        }
    }
}
