package com.springAWS.project.aws.spring.intrgration.model;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;
@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.springAWS.project.aws.spring.intrgration.service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        log.info(proceedingJoinPoint.getSignature()  + " got processed in "+end+"s");
        return result;
    }

    @Before("execution(* com.springAWS.project.aws.spring.intrgration.service..*(..))")
    public void logBefore(JoinPoint joinPoint){
        log.info("Entering method : "+joinPoint.getSignature());
    }

    @After("execution(* com.springAWS.project.aws.spring.intrgration.service..*(..))")
    public void afterExecution(JoinPoint joinPoint , Object result){
        log.info("Successfully completed execution of : "+joinPoint.getSignature());
    }

}
