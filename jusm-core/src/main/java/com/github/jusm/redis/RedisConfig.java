package com.github.jusm.redis;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.parser.ParserConfig;
import com.github.jusm.autoconfigure.UsmAutoConfiguration;

import redis.clients.jedis.Jedis;

@EnableCaching
@AutoConfigureBefore({ UsmAutoConfiguration.class })
@ConditionalOnClass({ JedisConnection.class, RedisOperations.class, Jedis.class })
@ImportAutoConfiguration(value = { RedisHttpSessionConfig.class })
@EnableConfigurationProperties(CacheProperties.class)
@Configuration
public class RedisConfig {

	// private Logger logger = LoggerFactory.getLogger(getClass());

	@Bean
	@ConditionalOnProperty(name = "usm.redis.cache.evict", havingValue = "true", matchIfMissing = true)
	public UsmRedisCacheCleanJob usmRedisCacheCleanJob() {
		return new UsmRedisCacheCleanJob();
	}

	/**
	 * redisTemplate 序列化使用的jdkSerializeable, 存储二进制字节码, 所以自定义序列化类
	 * 
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> fastJsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		// 设置键（key）的序列化采用StringRedisSerializer。
		StringRedisSerializer serializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(serializer);
		// 全局开启AutoType，不建议使用
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
		// 建议使用这种方式，小范围指定白名单
		// ParserConfig.getGlobalInstance().addAccept("com.xiaolyuh.");
		redisTemplate.setHashKeySerializer(serializer);
		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
		// 设置值（value）的序列化采用FastJsonRedisSerializer。
		redisTemplate.setValueSerializer(fastJsonRedisSerializer);
		redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean
	public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> fastJsonRedisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(fastJsonRedisTemplate);
		cacheManager.setUsePrefix(true);
		return cacheManager;
	}

	@Bean
	public RedisRepository redisRepository(StringRedisTemplate stringRedisTemplate,
			RedisTemplate<String, Object> fastJsonRedisTemplate, RedisTemplate<Object, Object> redisTemplate,
			JedisConnectionFactory jedisConnectionFactory) {
		return new RedisRepository(stringRedisTemplate, fastJsonRedisTemplate, redisTemplate,
				jedisConnectionFactory);
	}

}
