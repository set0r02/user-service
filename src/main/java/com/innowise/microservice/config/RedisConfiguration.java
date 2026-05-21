package com.innowise.microservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
        GenericJacksonJsonRedisSerializer genericJacksonJsonRedisSerializer = GenericJacksonJsonRedisSerializer.builder()
                .typePropertyName("@class")
                .build();

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(20))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJacksonJsonRedisSerializer));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .withCacheConfiguration("users", redisCacheConfiguration.entryTtl(Duration.ofHours(24)))
                .withCacheConfiguration("cards", redisCacheConfiguration.entryTtl(Duration.ofHours(1)))
                .withCacheConfiguration("paymentCards", redisCacheConfiguration.entryTtl(Duration.ofMinutes(15)))
                .build();
    }
}
