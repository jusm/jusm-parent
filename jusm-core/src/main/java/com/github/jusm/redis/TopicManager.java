package com.github.jusm.redis;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import com.github.jusm.security.UsmContext;
import com.github.jusm.util.CopyOnWriteMap;
import com.github.jusm.util.SpringContextHolder;


@Component
public class TopicManager {

	public Map<String, Set<String>> topicRouter = new CopyOnWriteMap<>();

	public synchronized boolean addRouter(String channelTopic, String beanId) {
		Class<? extends Object> clazz = SpringContextHolder.getBean(beanId).getClass();
		Class<?>[] interfaces = clazz.getInterfaces();
		Class<?> superclass = clazz.getSuperclass();
		if (Arrays.asList(interfaces).contains(MessageHandler.class) || superclass == AbstractMessageHandler.class) {
			Set<String> set = topicRouter.get(channelTopic);
			if (set == null) {
				set = new HashSet<>();
			}
			set.add(beanId);
			topicRouter.put(channelTopic, set);
			return true;
		}
		return false;
	}

	public Set<String> getMessageHandler(String channelTopic) {
		return topicRouter.get(channelTopic);
	}

	public Collection<ChannelTopic> channelTopics() {
		Collection<ChannelTopic> topics = new HashSet<>();
		Map<String, String> topicMap = UsmContext.getTopicMap();
		if (topicMap != null && !topicMap.isEmpty()) {
			for (Iterator<String> iterator = topicMap.values().iterator(); iterator.hasNext();) {
				ChannelTopic ct1 = new ChannelTopic(iterator.next());
				topics.add(ct1);
			}
		}
		return topics;
	}

}
