package com.github.jusm.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.jusm.entity.EmailAccount;
import com.github.jusm.entity.Permission;
import com.github.jusm.entity.User;
import com.github.jusm.repository.PermissionRepository;
import com.github.jusm.repository.UserRepository;
import com.github.jusm.service.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {
	
	@Autowired
	@Qualifier("fastJsonRedisTemplate")
	private RedisTemplate<String, Object> fastJsonRedisTemplate;
	

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	private String key ="usm:test:";
	@Test
	public void save() {
		EmailAccount emailAccount = new EmailAccount();
		emailAccount.setUsername("张三");
		emailAccount.setPlace("8045645@qq.com");
		emailAccount.setPassword("1234");
		
		
		EmailAccount emailAccount1 = new EmailAccount();
		emailAccount1.setUsername("张三");
		emailAccount1.setPlace("8045645@qq.com");
		emailAccount1.setPassword("1234");
		fastJsonRedisTemplate.opsForValue().set(key+"t",emailAccount);
		Object object = fastJsonRedisTemplate.opsForValue().get(key+"t");
		System.err.println(object instanceof EmailAccount);
		fastJsonRedisTemplate.opsForValue().set(key+"p",permissionRepository.findAll().get(0));
		
		object = fastJsonRedisTemplate.opsForValue().get(key+"p");
	    System.err.println(object instanceof Permission);
	    
	    
		fastJsonRedisTemplate.opsForValue().set(key+"u",userRepository.findAll().get(0));
		object = fastJsonRedisTemplate.opsForValue().get(key+"u");
	    System.err.println(object instanceof User);
		User new_name = (User) object;
		
		User s = userService.findByUsername("root");
		System.out.println(s instanceof User);
	}
}
