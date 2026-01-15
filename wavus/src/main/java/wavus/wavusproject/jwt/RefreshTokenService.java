package wavus.wavusproject.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    private String key(String userId) {
        return "refresh:"+userId;
    }

    public void save(String userId, String refreshToken , Duration ttl){
        redisTemplate.opsForValue().set(key(userId), refreshToken, ttl);
    }

    public String get(String userId){
        return redisTemplate.opsForValue().get(key(userId));
    }

    public void delete(String userId){
        redisTemplate.delete(key(userId));
    }

}
