package com.main21.security.utils;

import com.main21.exception.ExceptionCode;
import com.main21.security.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis Util 클래스
 * @author mozzi327
 */
@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    /**
     * redis에서 key-value를 저장하는 메서드
     * @param key REFRESH_TOKEN
     * @param o String(key)
     * @author mozzi327
     */
    public void setData(String key, Object o, int minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        redisTemplate.opsForValue().set(key, o, minutes, TimeUnit.MINUTES);
    }

    /**
     * redis에서 key에 대한 value 값을 가져오는 메서드
     * @param key REFRESH_TOKEN
     * @return String(key)
     * @author mozzi327
     */
    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * redis에서 key에 대한 row를 삭제하는 메서드
     * @param key REFRESH_TOKEN
     * @author mozzi327
     */
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    /**
     * redis 로그아웃 처리를 위한 블랙 리스트 메서드
     * @param key REFRESH_TOKEN
     * @param setTime 만료시간(분)
     * @author mozzi327
     */
    public void setBlackList(String key, Object o, Long setTime) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        redisBlackListTemplate.opsForValue().set(key, o, setTime, TimeUnit.MILLISECONDS);
    }

    /**
     * accessToken 블랙리스트 여부 조회 메서드(로그아웃)
     * @param key accessToken
     * @return accssToken
     * @author mozzi327
     */
    public String isBlackList(String key) {
        return (String) redisBlackListTemplate.opsForValue().get(key);
    }

    /**
     * 리프레시 토큰을 통해 memberId를 조회하는 메서드(논의 필요)
     * @param refreshToken 리프레시 토큰
     * @return Long(memberId)
     * @author mozzi327
     */
    public Long getId(String refreshToken) {
        Long memberId = (Long) redisTemplate.opsForValue().get(refreshToken);
        if (memberId == null) throw new AuthException(ExceptionCode.INVALID_REFRESH_TOKEN);
        return memberId;
    }
}
