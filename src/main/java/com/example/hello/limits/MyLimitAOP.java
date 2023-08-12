package com.example.hello.limits;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
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
        RRateLimiter rateLimiter = redisson.getRateLimiter(ofRateLimiter);
        // 创建令牌桶数据模型
        if (!rateLimiter.isExists()) {
            rateLimiter.trySetRate(limit.mode(), limit.count(), limit.period(), RateIntervalUnit.SECONDS);
        }

        // permits 允许获得的许可数量 (如果获取失败，返回false) 1秒内不能获取到1个令牌，则返回，不阻塞
        // 尝试访问数据，占数据计算值var1，设置等待时间var3
        // acquire() 默认如下参数 如果超时时间为-1，则永不超时，则将线程阻塞，直至令牌补充
        if (!rateLimiter.tryAcquire(1, 1, TimeUnit.MILLISECONDS)) {
            System.out.println("???");
            return ResponseEntity.status(429).body("{\"code\":\"429\", \"msg\":\"Too many requests!\"}");
        }
        return point.proceed();
    }
}