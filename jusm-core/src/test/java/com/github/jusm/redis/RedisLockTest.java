package com.github.jusm.redis;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisLockTest {

	private int count = 100;

	@Resource(name = "redisLock")
	private Lock lock;

	// private Lock lock = new ReentrantLock();

	// @Test
	// public void del12() throws InterruptedException {
	// stringRedisTemplate.delete("wen1");
	// String andSet = stringRedisTemplate.opsForValue().getAndSet("wen", "122");
	//
	// System.out.println(andSet.toString());
	// stringRedisTemplate.delete("wen");
	// }
	// @Test
	// public void del() throws InterruptedException {
	// Object andSet = redisTemplate.opsForValue().getAndSet("hao", "122");
	//
	// System.out.println(andSet.toString());
	// }

	public class TicketGen implements Runnable {
		@Override
		public void run() {
			lock.lock();
			while (count > 0) {
				try {
					if (count > 0) {
						System.out.println(Thread.currentThread().getName() + "销售第" + (count--) + "账票");
					}
				} finally {
					lock.unlock();
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	@Test
	public void sold() throws InterruptedException {
		TicketGen ticketGen = new TicketGen();
		Thread t1 = new Thread(ticketGen, "窗口A");
		Thread t2 = new Thread(ticketGen, "窗口B");
		Thread t3 = new Thread(ticketGen, "窗口C");
		Thread t4 = new Thread(ticketGen, "窗口D");
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		Thread.currentThread().join();
	}
}
