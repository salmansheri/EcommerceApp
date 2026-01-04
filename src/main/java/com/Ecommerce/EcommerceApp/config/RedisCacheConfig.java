package com.Ecommerce.EcommerceApp.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer.GenericJackson2JsonRedisSerializerBuilder;

@Configuration
public class RedisCacheConfig {

	@Bean
	RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration defaults = RedisCacheConfiguration.defaultCacheConfig()

			.entryTtl(Duration.ofMinutes(60))
			.enableTimeToIdle();

		return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(defaults).build();

	}

}
