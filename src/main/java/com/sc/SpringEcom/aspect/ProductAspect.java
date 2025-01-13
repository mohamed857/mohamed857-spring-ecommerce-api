package com.sc.SpringEcom.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ProductAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProductAspect.class);

    @Around("execution(* com.sc.SpringEcom.service.getAllProducts(..))")
    public Object monitorTime(ProceedingJoinPoint jp) throws Throwable {

        Long start = System.currentTimeMillis();
        Object obj = jp.proceed();
        Long end = System.currentTimeMillis();
        LOGGER.info("Time Taken By :"+jp.getSignature().getName()+" is "+(end-start)+" ms" );

        return obj;
    }

}
