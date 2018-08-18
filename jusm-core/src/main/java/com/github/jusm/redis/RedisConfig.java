package com.github.jusm.redis;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
public class RedisConfig {

	@Autowired
	private RedisConnectionFactory factory;

	@Bean
	public RedisTemplate<String, Serializable> redisTemplate() {
		RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setConnectionFactory(factory);
		return redisTemplate;
	}



	@Bean
	@Primary
	public Publisher initPublisher(@Autowired RedisTemplate<String, Serializable> redisTemplate) {
		return new Publisher(redisTemplate);
	}

	@Bean
	@Primary
	public SubscriberDispatcher initSubscriber(@Autowired TopicManager topicManager) {
		return new SubscriberDispatcher(topicManager);
	}

	@Bean
	public HashOperations<String, String, Serializable> hashOperations(
			RedisTemplate<String, Serializable> redisTemplate) {
		return redisTemplate.opsForHash();
	}

	@Bean
	public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
		return redisTemplate.opsForValue();
	}

	@Bean
	public ListOperations<String, Serializable> listOperations(RedisTemplate<String, Serializable> redisTemplate) {
		return redisTemplate.opsForList();
	}

	@Bean
	public SetOperations<String, Serializable> setOperations(RedisTemplate<String, Serializable> redisTemplate) {
		return redisTemplate.opsForSet();
	}

	@Bean
	public ZSetOperations<String, Serializable> zSetOperations(RedisTemplate<String, Serializable> redisTemplate) {
		return redisTemplate.opsForZSet();
	}

	@Bean
	public StringRedisSerializer stringRedisSerializer() {
		return new StringRedisSerializer();
	}

	@Bean
	public JdkSerializationRedisSerializer jdkSerializationRedisSerializer() {
		return new JdkSerializationRedisSerializer();
	}

	@Bean
	@Autowired
	public MessageListenerAdapter messageListenerAdapter(MessageDelegateHandler delegate,
			StringRedisSerializer keySerializer, JdkSerializationRedisSerializer valueSerializer) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
		messageListenerAdapter.setDefaultListenerMethod(MessageListenerAdapter.ORIGINAL_DEFAULT_LISTENER_METHOD);
		messageListenerAdapter.setDelegate(delegate);
		messageListenerAdapter.setStringSerializer(keySerializer);
		messageListenerAdapter.setSerializer(valueSerializer);
		return messageListenerAdapter;
	}

	@Bean
	@Autowired
	public RedisMessageListenerContainer redisMessageListenerContainer(TopicManager topicManager,
			RedisConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
		RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
		redisMessageListenerContainer.setConnectionFactory(connectionFactory);
		redisMessageListenerContainer.addMessageListener(messageListenerAdapter, topicManager.channelTopics());
		return redisMessageListenerContainer;
	}

	@Bean
	@Autowired
	public RedisUtil redisUtil(RedisTemplate<String, Serializable> redisTemplate,
			ValueOperations<String, String> valueOperations,
			HashOperations<String, String, Serializable> hashOperations,
			ListOperations<String, Serializable> listOperations, SetOperations<String, Serializable> setOperations,
			ZSetOperations<String, Serializable> zSetOperations) {
		return new RedisUtil(redisTemplate, valueOperations, hashOperations, listOperations, setOperations,
				zSetOperations);
	}


}
