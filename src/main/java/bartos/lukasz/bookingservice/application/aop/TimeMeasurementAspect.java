package bartos.lukasz.bookingservice.application.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeMeasurementAspect {

    @Around("@annotation(bartos.lukasz.bookingservice.application.annotations.TimeMeasurement) && execution(public * * (..))")
    public Object measureTime(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        var methodStartTime = System.currentTimeMillis();
        Object value = null;
        try {
            value = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("ERROR while executing method. MESSAGE: " + throwable.getMessage());
        } finally {
            var methodStopTime = System.currentTimeMillis();
            var duration = methodStopTime - methodStartTime;
            log.info("\n\nMETHOD: " + proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName() +
                    "." + proceedingJoinPoint.getSignature().getName() + " with duration " + duration
             + "ms\n");
        }
        return value;
    }
}
