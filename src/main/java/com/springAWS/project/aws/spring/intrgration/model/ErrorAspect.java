package com.springAWS.project.aws.spring.intrgration.model;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ErrorAspect {

    @AfterThrowing("execution(* com.springAWS.project.aws.spring.intrgration.service..*(..))")
    public void logException(JoinPoint joinPoint , Throwable ex){
        String METHOD_EXCEPTION = "Method named:";
        String message = METHOD_EXCEPTION.concat(joinPoint.getSignature().toString()).concat("Returned exception : ").concat(ex.getLocalizedMessage());
        log.error(message);
    }
}
