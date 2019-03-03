package com.github.jusm.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisBatchOperationsTest {

	@Autowired
	private RedisBatchOperations redisBatchOperations;

	@Autowired
	private RedisRepository redisRepository;

	private static Map<byte[], byte[]> map = new HashMap<>();

	/**
	 * 
	 */
	@BeforeClass
	public static void initTestData() {
		for (int i = 0; i < 100000; i++) {
			String string = "key:" + i;
			String string2 = "value:" + i;
			map.put(string.getBytes(), string2.getBytes());
		}
	}

	@Test
	public void testSet() {
		long t = System.currentTimeMillis();
		redisRepository.set(map);
		System.out.println( System.currentTimeMillis()- t);
	}

	@Test
	public void testDelete() {
		long t = System.currentTimeMillis();
//		redisRepository.delete((byte[][])(new ArrayList<byte[]>(map.keySet()).toArray()));
		System.out.println(System.currentTimeMillis()- t);
	}
}
