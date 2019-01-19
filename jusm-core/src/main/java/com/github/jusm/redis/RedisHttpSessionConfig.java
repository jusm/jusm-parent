package com.github.jusm.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.github.jusm.util.Conts;

/**
 * <B> 原Boot的server.session.timeout属性不再生效。 </B>
 * 
 * @author wen
 *
 */
@Configuration
@ConditionalOnClass(EnableRedisHttpSession.class)
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = Conts.HTTPSESSION_TIMEOUT) // session过期时间 如果部署多机环境,需要打开注释
@ConditionalOnProperty(name = "spring.session.store-type", havingValue = "redis", matchIfMissing = false)
public class RedisHttpSessionConfig {

}
