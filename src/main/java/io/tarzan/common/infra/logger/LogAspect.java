package io.tarzan.common.infra.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {

    /**
     * 定义一个切入点.
     * 解释下：
     * <p>
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 定义在web包或者子包
     * ~ 第三个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */
    @Pointcut("execution(* com.ruike..*.api.controller..*(..))")
    public void logPointcut() {
    }

    @org.aspectj.lang.annotation.Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //LOG.debug("logPointcut " + joinPoint + "\t");
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            log.info("LogAspect+++++around " + joinPoint + "\tUse time : "
                    + ((end - start) / 1000) + "ms !LogAspect");
            return result;
        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            log.error("LogAspect+++++around " + joinPoint + "\tUse time : "
                    + ((end - start) / 1000) + "ms with exception :" + e.getMessage() + "!LogAspect");
            throw e;
        }
    }

}
