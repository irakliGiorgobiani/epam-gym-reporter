package com.epam.epamgymreporter.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(com.epam.epamgymreporter.controller..*)")
    public void controllerPointcut() {}

    @Pointcut("within(com.epam.epamgymreporter.service..*)")
    public void servicePointcut() {}

    @Before("controllerPointcut()")
    public void logTransactionStart(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        String transactionId = UUID.randomUUID().toString();

        request.setAttribute("transactionId", transactionId);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String requestURI = request.getRequestURI();

        log.info("Transaction Start: [transactionId={}], [endpoint={}], [method={}], [requestURI={}]",
                transactionId, request.getMethod(), methodName, requestURI);
    }

    @AfterReturning(value = "controllerPointcut()", returning = "response")
    public void logTransactionSuccess(JoinPoint joinPoint, ResponseEntity<?> response) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String transactionId = (String) request.getAttribute("transactionId");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.info("Transaction Success: [transactionId={}], [method={}], [status={}], [response={}]",
                transactionId, methodName, response.getStatusCode(), response.getBody());
    }

    @AfterThrowing(value = "controllerPointcut()", throwing = "exception")
    public void logTransactionFailure(JoinPoint joinPoint, Exception exception) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String transactionId = (String) request.getAttribute("transactionId");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.error("Transaction Failure: [transactionId={}], [method={}], [exception={}]",
                transactionId, methodName, exception.getMessage(), exception);
    }

    @Before("servicePointcut()")
    public void logOperationStart(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String transactionId = (String) request.getAttribute("transactionId");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.info("Operation Start: [transactionId={}], [method={}]", transactionId, methodName);
    }

    @AfterReturning("servicePointcut()")
    public void logOperationSuccess(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String transactionId = (String) request.getAttribute("transactionId");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.info("Operation Success: [transactionId={}], [method={}]", transactionId, methodName);
    }

    @AfterThrowing(value = "servicePointcut()", throwing = "exception")
    public void logOperationFailure(JoinPoint joinPoint, Exception exception) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String transactionId = (String) request.getAttribute("transactionId");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.error("Operation Failure: [transactionId={}], [method={}], [exception={}]",
                transactionId, methodName, exception.getMessage(), exception);
    }
}
