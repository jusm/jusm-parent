package com.github.jusm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.jusm.model.R;
import com.github.jusm.redis.RedisLock;

@RestController
public class RedisController extends ApiController {

	@Autowired
	private RedisLock redisLock;

	@PostMapping("redis/lock")
	public R con() {
		redisLock.lock();
		return R.success();
	}
	
	@PostMapping("redis/unlock")
	public R unlock() {
		redisLock.unlock();
		return R.success();
	}

}
