package com.bocian.quizzes.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class ArgsDebugAspect {

    @Value("${bocian.quizzes.args.debug.enabled:false}")
    private boolean argsDebuggingEnabled;

    @Pointcut("execution(public * com.bocian.quizzes..*(..))")
    private void onAnyPublicMethodWithArgs() {}

    @Before("onAnyPublicMethodWithArgs()")
    public void logInputArgs(final JoinPoint joinPoint) {
        if (argsDebuggingEnabled && joinPoint.getArgs().length > 0) {
            log.info("Arguments of " + joinPoint.getSignature() + ":\n" + Arrays.toString(joinPoint.getArgs()));
        }
    }
}
