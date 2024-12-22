package com.aeon.library.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.aeon.library.controller..*(..))")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        // Log method entry with request details
        logger.info("Request received: {} in controller: {} with arguments: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getName(),
                Arrays.toString(joinPoint.getArgs()));

        // Proceed with the actual method execution
        Object result = joinPoint.proceed();

        // Log the response (after the method has completed)
        logger.info("Response sent from : {} in controller: {} returned: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getName(),
                result instanceof ResponseEntity ? ((ResponseEntity<?>) result).getBody() : result);

        return result;
    }
}
