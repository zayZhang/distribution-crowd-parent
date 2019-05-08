package com.zay.crowd.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Test
	public void testString() {
		String string = stringRedisTemplate.opsForValue().get("stringRedisName");
		System.out.println("value=  "+string);
	}
}
