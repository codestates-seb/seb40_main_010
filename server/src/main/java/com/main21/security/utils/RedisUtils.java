package com.main21.security.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisUtils {
    private StringRedisTemplate stringRedisTemplate;
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
        stringRedisTemplate.delete(key);
    }


    /**
     * redis 로그아웃 처리를 위한 블랙 리스트 메서드
     * @param key REFRESH_TOKEN
     * @param o
     * @param minutes
     */
    public void setBlackList(String key, Object o, int minutes) {
        redisBlackListTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        redisBlackListTemplate.opsForValue().set(key, o, minutes, TimeUnit.MINUTES);
    }
}
