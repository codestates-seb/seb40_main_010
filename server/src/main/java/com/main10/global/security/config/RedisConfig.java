package com.main10.global.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 설정 정보 Configuration 클래스
 * @author mozzi327
 */
//@EnableCaching
@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    /*
        Lettuce: Multi-Thread 에서 Thread-Safe한 Redis 클라이언트로 netty에 의해 관리된다.
                 Sentinel, Cluster, Redis data model 같은 고급 기능들을 지원하며
                 비동기 방식으로 요청하기에 TPS/CPU/Connection 개수와 응답속도 등 전 분야에서 Jedis 보다 뛰어나다.
                 스프링 부트의 기본 의존성은 현재 Lettuce로 되어있다.

        Jedis  : Multi-Thread 에서 Thread-unsafe 하며 Connection pool을 이용해 멀티쓰레드 환경을 구성한다.
                 Jedis 인스턴스와 연결할 때마다 Connection pool을 불러오고 스레드 갯수가
                 늘어난다면 시간이 상당히 소요될 수 있다.
     */

    /**
     * RedisTemplate을 이용하여 redis를 Connection하는 메서드<br>
     * RedisConnectionFactory 인터페이스를 통해<br>
     * LettuceConnectionFactory를 생성하여 반환<br>
     * @return LettuceConnectionFactory
     * @author mozzi327
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    /**
     * redis의 자료구조를 사용하기 위한 메서드
     * @return RedisTemplete
     * @author mozzi327
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        // redisTemplate를 받아와서 set, get, delete를 사용
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // setKeySerializer, setValueSerializer 설정
        // redis-cli를 통해 직접 데이터를 조회 시 알아볼 수 없는 형태로 출력되는 것을 방지
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }
}
