package org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class.getName());

    @Before("@annotation(org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingLogging)")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Starting createTask method " + joinPoint.getSignature());
    }

    @Around("@annotation(org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingTracking)")
    public Object trackingAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Logic before executing the method " + joinPoint.getSignature());
        long startTime = System.currentTimeMillis();

        try {
            Object proceed = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("Method " + joinPoint.getSignature().getName() + " executed in " + executionTime + " ms");
            return proceed;
        } catch (Throwable throwable) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("Method " + joinPoint.getSignature().getName() + " threw an exception after " + executionTime + " ms: " + throwable.getMessage(), throwable);
            throw throwable;
        }
    }

    @AfterReturning(
            pointcut = "@annotation(org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.HandlingResult)",
            returning = "result"
    )
    public void handleResult(Task result) {
        logger.info("getTask method executed successfully. Returned value: " + result);
    }

    @AfterThrowing(pointcut = "@annotation(org.likhanov_2011.task_management_service_OnlineScooll_T1.aspect.annotation.ExceptionHandling)",
            throwing = "tr")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable tr) throws Throwable {
        logger.info("Exception caught in " + joinPoint.getSignature().getName());
        logger.info("Exception type is " + tr.getClass().getName());
    }
}
