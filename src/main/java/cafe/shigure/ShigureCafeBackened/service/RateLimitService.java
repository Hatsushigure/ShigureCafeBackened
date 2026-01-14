package cafe.shigure.ShigureCafeBackened.service;

import cafe.shigure.ShigureCafeBackened.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final StringRedisTemplate redisTemplate;

    public void checkRateLimit(String key, int seconds) {
        String fullKey = "ratelimit:" + key;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(fullKey))) {
            Long expire = redisTemplate.getExpire(fullKey, TimeUnit.SECONDS);
            throw new BusinessException("RATE_LIMIT_EXCEEDED",
                    Map.of("retryAfter", expire != null ? expire : seconds));
        }
        redisTemplate.opsForValue().set(fullKey, "1", seconds, TimeUnit.SECONDS);
    }
}
