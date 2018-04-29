package com.bocian.quizzes.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodExecutionTimeAspect {

    @Around("@annotation(com.bocian.quizzes.aspects.annotations.Measured)")
    public Object logExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {

        final long startTime = System.currentTimeMillis();
        final Object proceed = joinPoint.proceed();
        final long methodExecutionTime = System.currentTimeMillis() - startTime;
        log.debug(joinPoint.getSignature() + " took " + methodExecutionTime + " milliseconds");
        return proceed;
    }
}
