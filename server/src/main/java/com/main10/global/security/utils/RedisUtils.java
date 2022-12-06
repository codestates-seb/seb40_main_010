package com.main10.global.security.utils;

import com.main10.global.exception.ExceptionCode;
import com.main10.global.security.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.concurrent.TimeUnit;

/**
 * Redis Util 클래스
 * @author mozzi327
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RedisUtils {
    private final StringRedisTemplate redisTemplate;
    private final StringRedisTemplate redisBlackListTemplate;

    /**
     * redis에서 key-value를 저장하는 메서드
     * @param key REFRESH_TOKEN
     * @param o String(key)
     * @author mozzi327
     */
    public void setData(String key, String provider ,String  o, int minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        String refreshKey = provider + "RT :" + key;
        redisTemplate.opsForValue().set(refreshKey, o, minutes * 60L, TimeUnit.SECONDS);
    }

    /**
     * redis에서 key에 대한 value 값을 가져오는 메서드
     * @param key REFRESH_TOKEN
     * @return String(key)
     * @author mozzi327
     */
    public Object getData(String key, String provider) {
        String refreshKey = provider + "RT :" + key;
        return redisTemplate.opsForValue().get(refreshKey);
    }

    /**
     * redis에서 key에 대한 row를 삭제하는 메서드
     * @param key REFRESH_TOKEN
     * @author mozzi327
     */
    public void deleteData(String key, String provider) {
        String refreshKey = provider + "RT :" + key;
        redisTemplate.delete(refreshKey);
    }

    /**
     * redis 로그아웃 처리를 위한 블랙 리스트 메서드
     * @param key REFRESH_TOKEN
     * @param setTime 만료시간(분)
     * @author mozzi327
     */
    public void setBlackList(String key, String o, Long setTime) {
        ValueOperations<String, String> stringStringValueOperations = redisBlackListTemplate.opsForValue();
        redisBlackListTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        stringStringValueOperations.set(key, o, setTime, TimeUnit.MILLISECONDS);
    }

    /**
     * accessToken 블랙리스트 여부 조회 메서드(로그아웃)
     * @param key accessToken
     * @return accssToken
     * @author mozzi327
     */
    public String isBlackList(String key) {
        ValueOperations<String, String> stringValueOperations = redisBlackListTemplate.opsForValue();
        return stringValueOperations.get(key);
    }

    /**
     * 리프레시 토큰을 통해 memberId를 조회하는 메서드(논의 필요)
     * @param refreshToken 리프레시 토큰
     * @return Long(memberId)
     * @author mozzi327
     */
    public Long getId(String refreshToken) {
        if (!StringUtils.hasText(refreshToken))
            throw new AuthException(ExceptionCode.INVALID_REFRESH_TOKEN);
        ValueOperations<String, String> stringValueOperations = redisBlackListTemplate.opsForValue();
        String id = String.valueOf(stringValueOperations.get(refreshToken));
        return Long.valueOf(id);
    }
}
