package com.main21.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Configuration
public class EmbeddedRedisConfig {
    private final int port = 6378; // 테스트 포트
    private RedisServer redisServer;


    @PostConstruct
    public void setRedisServer() throws Exception {
        redisServer = new RedisServer(isRedisRunning() ? findAvailablePort() : port);
        redisServer.start();
    }


    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) redisServer.stop();
    }

    private boolean isRedisRunning() throws Exception {
        return isRunning(executeGrepProcessCommand(port));
    }

    private Process executeGrepProcessCommand(int port) throws Exception {
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }


    private int findAvailablePort() throws Exception {
        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception e) {
        }

        return StringUtils.hasLength(pidInfo.toString());
    }
}
