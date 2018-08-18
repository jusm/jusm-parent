package com.github.jusm.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 集群session共享
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400 * 30)//session过期时间  如果部署多机环境,需要打开注释
@ConditionalOnProperty(prefix = "usm", name = "spring.session.store-type", havingValue = "redis",matchIfMissing=false)
public class RedisHttpSessionConfig {
}
