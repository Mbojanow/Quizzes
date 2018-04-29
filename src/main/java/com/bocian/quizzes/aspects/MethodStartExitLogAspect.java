package com.bocian.quizzes.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodStartExitLogAspect {

    @Around("@annotation(com.bocian.quizzes.aspects.annotations.ExecutionLogged)")
    public Object logStartAndExit(final ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("Start of " + joinPoint.getSignature());
        final Object proceed = joinPoint.proceed();
        log.debug("Exit of " + joinPoint.getSignature());
        return proceed;
    }
}
