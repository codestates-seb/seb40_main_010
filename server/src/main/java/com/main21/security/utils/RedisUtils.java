package com.main21.security.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtils {
    private StringRedisTemplate stringRedisTemplate;


    /**
     * redis에서 key에 대한 value 값을 가져오는 메서드
     * @param key REFRESH_TOKEN
     * @return String(key)
     * @author mozzi327
     */
    public String getData(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }


    /**
     * redis에서 key-value를 저장하는 메서드
     * @param key REFRESH_TOKEN
     * @param value String(key)
     * @author mozzi327
     */
    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }


    /**
     * redis에서 key에 대한 row를 삭제하는 메서드
     * @param key REFRESH_TOKEN
     * @author mozzi327
     */
    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }
}
