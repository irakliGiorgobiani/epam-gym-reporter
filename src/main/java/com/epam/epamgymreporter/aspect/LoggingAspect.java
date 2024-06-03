package com.epam.epamgymreporter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(com.epam.epamgymreporter.service..*)")
    public void servicePointcut() {}

    @Before("servicePointcut()")
    public void logOperationStart(JoinPoint joinPoint) {
        String transactionId = UUID.randomUUID().toString();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.info("Operation Start: [transactionId={}], [method={}]", transactionId, methodName);
    }

    @AfterReturning("servicePointcut()")
    public void logOperationSuccess(JoinPoint joinPoint) {
        String transactionId = UUID.randomUUID().toString();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.info("Operation Success: [transactionId={}], [method={}]", transactionId, methodName);
    }

    @AfterThrowing(value = "servicePointcut()", throwing = "exception")
    public void logOperationFailure(JoinPoint joinPoint, Exception exception) {
        String transactionId = UUID.randomUUID().toString();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.error("Operation Failure: [transactionId={}], [method={}], [exception={}]",
                transactionId, methodName, exception.getMessage(), exception);
    }
}
