//package com.github.jusm.redis;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.SetOperations;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.data.redis.core.ZSetOperations;
//import org.springframework.data.redis.support.atomic.RedisAtomicLong;
//
//import com.alibaba.fastjson.JSONObject;
//
//
///**
// * Redis工具类
// */
//public class RedisUtil {
//	private RedisTemplate<Object, Object> redisTemplate;
//	private ValueOperations<String, String> valueOperations;
//	@SuppressWarnings("unused")
//	private HashOperations<String, String, Serializable> hashOperations;
//	@SuppressWarnings("unused")
//	private ListOperations<String, Serializable> listOperations;
//	@SuppressWarnings("unused")
//	private SetOperations<String, Serializable> setOperations;
//	@SuppressWarnings("unused")
//	private ZSetOperations<String, Serializable> zSetOperations;
//	
//	
//	public RedisUtil(RedisTemplate<Object, Object> redisTemplate, ValueOperations<String, String> valueOperations,
//			HashOperations<String, String, Serializable> hashOperations, ListOperations<String, Serializable> listOperations,
//			SetOperations<String, Serializable> setOperations, ZSetOperations<String, Serializable> zSetOperations) {
//		this.redisTemplate = redisTemplate;
//		this.valueOperations = valueOperations;
//		this.hashOperations = hashOperations;
//		this.listOperations = listOperations;
//		this.setOperations = setOperations;
//		this.zSetOperations = zSetOperations;
//	}
//
//	/** 默认过期时长，单位：秒 */
//	public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
//	/** 不设置过期时长 */
//	public final static long NOT_EXPIRE = -1;
////	private final static Gson gson = new Gson();
//
//	public void set(String key, Object value, long expire) {
//		valueOperations.set(key, toJson(value));
//		if (expire != NOT_EXPIRE) {
//			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//		}
//	}
//
//	public void set(String key, Object value) {
//		set(key, value, DEFAULT_EXPIRE);
//	}
//
//	public Long getExpire(String key){
//		return redisTemplate.getExpire(key);
//	}
//
//	/**
//	 * 基于0 先增后取  为 1  
//	 * @param key 
//	 * @return
//	 */
//	public long incrementAndGet(String key) {
//		RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//		return counter.incrementAndGet();
//	}
//	
//	/**
//	 * 
//	 * @param key
//	 * @param expireTime
//	 * @return
//	 */
//	public long incrementAndGet(String key, Date expireTime) {
//		RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//		counter.expireAt(expireTime);
//		return counter.incrementAndGet();
//	}
//
//	public long incrementAndGet(String key, Long expire) {
//		RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//		counter.expire(expire, TimeUnit.SECONDS);
//		return counter.incrementAndGet();
//	}
//
//
//	public <T> T get(String key, Class<T> clazz, long expire) {
//		String value = valueOperations.get(key);
//		if (expire != NOT_EXPIRE) {
//			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//		}
//		return value == null ? null : fromJson(value, clazz);
//	}
//
//	public <T> T get(String key, Class<T> clazz) {
//		return get(key, clazz, NOT_EXPIRE);
//	}
//
//	public String get(String key, long expire) {
//		String value = valueOperations.get(key);
//		if (expire != NOT_EXPIRE) {
//			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//		}
//		return value;
//	}
//
//	public String get(String key) {
//		return get(key, NOT_EXPIRE);
//	}
//
//	public void delete(String key) {
//		redisTemplate.delete(key);
//	}
//	
//	public boolean isMember(String key,Object o) {
//		return setOperations.isMember(key, o);
//	}
//	
//	public void add(String key,Serializable value) {
//		setOperations.add(key, value);
//	}
//	
//	public void add(String key,long expire, Serializable value) {
//		setOperations.add(key, value);
//		setOperations.getOperations().expire(key, expire, TimeUnit.SECONDS);
//	}
//
//	/**
//	 * Object转成JSON数据
//	 */
//	private String toJson(Object object) {
//		if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
//				|| object instanceof Boolean || object instanceof String) {
//			return String.valueOf(object);
//		}
//		return JSONObject.toJSONString(object);
//	}
//
//	/**
//	 * JSON数据，转成Object
//	 */
//	private <T> T fromJson(String json, Class<T> clazz) {
//		return JSONObject.parseObject(json, clazz);
//	}
//}
