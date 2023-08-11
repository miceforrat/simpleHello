package com.example.hello.redissonConfig;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redisson() throws IOException {
        System.out.println(System.getProperty("cur_stage"));
        if (System.getProperty("cur_stage").equals("bootJar")){
            return Redisson.create(
                    Config.fromYAML(new ClassPathResource("redisson-dev.yaml").getInputStream()));
        }
        return Redisson.create(
                Config.fromYAML(new ClassPathResource("redisson-dev-test.yaml").getInputStream()));
    }
}
