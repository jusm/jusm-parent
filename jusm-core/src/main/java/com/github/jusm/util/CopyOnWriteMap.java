package com.github.jusm.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * CopyOnWrite并发容器用于读多写少的并发场景。比如白名单，黑名单，商品类目的访问和更新场景，假如我们有一个搜索网站，用户在这个网站的搜索框中，
 * 输入关键字搜索内容，但是某些关键字不允许被搜索。这些不能被搜索的关键字会被放在一个黑名单当中，黑名单每天晚上更新一次。当用户搜索时，
 * 会检查当前关键字在不在黑名单当中，如果在，则提示不能搜索。实现代码如下：
 * 
 * @param <K>
 * @param <V>
 */
public class CopyOnWriteMap<K, V> implements Map<K, V>, Cloneable {
	private volatile Map<K, V> internalMap;
	transient final ReentrantLock lock = new ReentrantLock();///还可以用这个
	public CopyOnWriteMap() {
		internalMap = new HashMap<K, V>();
	}

	public V put(K key, V value) {
//		final ReentrantLock lock = this.lock;
//		lock.lock();
		synchronized (this) {
			Map<K, V> newMap = new HashMap<K, V>(internalMap);
			V val = newMap.put(key, value);
			internalMap = newMap;
			return val;
		}
		
		
	}

	public V get(Object key) {
		return internalMap.get(key);
	}

	public void putAll(Map<? extends K, ? extends V> newData) {
		synchronized (this) {
			Map<K, V> newMap = new HashMap<K, V>(internalMap);
			newMap.putAll(newData);
			internalMap = newMap;
		}
	}

	@Override
	public int size() {
		return internalMap.size();
	}

	@Override
	public boolean isEmpty() {
		return internalMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return internalMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return internalMap.containsValue(value);
	}

	@Override
	public V remove(Object key) {
		return internalMap.remove(key);
	}

	@Override
	public void clear() {
		internalMap.clear();
	}

	@Override
	public Set<K> keySet() {
		return internalMap.keySet();
	}

	@Override
	public Collection<V> values() {
		return internalMap.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return internalMap.entrySet();
	}
}