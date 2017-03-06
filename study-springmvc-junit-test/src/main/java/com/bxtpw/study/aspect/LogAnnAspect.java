package com.bxtpw.study.aspect;

import com.bxtpw.study.annotation.LogAnn;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Created by DW on 2017/3/6.
 */
@Aspect
@Component
public class LogAnnAspect {

    @AfterReturning(pointcut = "@annotation(logAnn)")
    public void logAnn(JoinPoint joinPoint, LogAnn logAnn) {
        System.out.println("LogAnn: " + logAnn.type() + ", " + logAnn.value());
    }
}
