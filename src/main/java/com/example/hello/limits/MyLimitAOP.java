package com.example.hello.limits;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateLimiterConfig;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class MyLimitAOP {
    private static final String REDIS_LIMIT_KEY_HEAD = "limit";
    @Autowired
    private RedissonClient redisson;

    // 切入点
    @Pointcut("@annotation(MyRedisRLimiter)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        MyRedisRLimiter limit = method.getAnnotation(MyRedisRLimiter.class);
        String key = StringUtils.upperCase(method.getName());
//        ImmutableList keys = ImmutableList.of(StringUtils.join(REDIS_LIMIT_KEY_HEAD, limit.prefix(), ":", ip, key));
        // 生成key
        final String ofRateLimiter = REDIS_LIMIT_KEY_HEAD + key + limit.key();
        RRateLimiter rateLimiter = getRateLimiter(limit, ofRateLimiter);

        // permits 允许获得的许可数量 (如果获取失败，返回false) 1秒内不能获取到1个令牌，则返回，不阻塞

        if (!rateLimiter.tryAcquire(1)) {
            return ResponseEntity.status(429).body("{\"code\":\"429\", \"msg\":\"Too many requests!\"}");
        }
        return point.proceed();
    }

    private RRateLimiter getRateLimiter(MyRedisRLimiter limit, String key){
        long count = limit.count();
        long interval = limit.period();
        RRateLimiter rateLimiter = redisson.getRateLimiter(key);
        // 创建令牌桶数据模型
        if (!rateLimiter.isExists()) {
            rateLimiter.trySetRate(limit.mode(), limit.count(), limit.period(), RateIntervalUnit.SECONDS);
            return rateLimiter;
        }

        RateLimiterConfig config = rateLimiter.getConfig();
        long lastInterval = config.getRateInterval();
        long timeByMilli=TimeUnit.MILLISECONDS.convert(interval, TimeUnit.SECONDS);
        long beforeRate = config.getRate();
        System.err.println("limiterName: " + key + "; count: " + count + "; interval: " + interval + "; beforeRate: " + beforeRate + "; lastInterval "+ lastInterval);
        if (beforeRate != count || timeByMilli !=lastInterval || !config.getRateType().equals(limit.mode())){
            System.err.println("Changed");
//            rateLimiter.delete();
            rateLimiter.trySetRate(limit.mode(), count, interval, RateIntervalUnit.SECONDS);
        }
        return rateLimiter;
    }
}