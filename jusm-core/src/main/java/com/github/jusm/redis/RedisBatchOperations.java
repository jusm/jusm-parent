package com.github.jusm.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * 
 * mysql -uroot -p dbname --default-charachter-set=utf8 --skip-column-names
 * --raw < order.sql | redis-cli -h localhost -p 6379 -a 123465 --pipe
 *
 */
public class RedisBatchOperations {

	@Autowired
	private JedisConnectionFactory factory;

	public Response<Long> delete(String... keys) {
		RedisConnection conn = null;
		try {
			conn = RedisConnectionUtils.getConnection(factory);
			Jedis jedis = (Jedis) conn.getNativeConnection();
			Pipeline pipelined = jedis.pipelined();
			pipelined.del(keys);
			return pipelined.del(keys);
		} finally {
			RedisConnectionUtils.releaseConnection(conn, factory);
		}
	}

}
