package com.zay.crowd.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zay.crowd.entity.ResultEntity;

@RestController
public class RedisOperationController {
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@RequestMapping("/remove/by/key")
	ResultEntity<String> removeByKey(@RequestParam("key") String key){
		try {
			redisTemplate.delete(key);
		}catch(Exception e) {
			e.printStackTrace();
			return ResultEntity.failed(e.getMessage());
		}
		return ResultEntity.successNoData();
	}
	
	@RequestMapping("/retrieve/string/value/by/string/key")
	public ResultEntity<String> retrieveStringValueByStringKey(@RequestParam("normalKey") String normalKey) {
		
		String stringValue = null;
		
		try {
			stringValue = redisTemplate.opsForValue().get(normalKey);
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResultEntity.failed(e.getMessage());
		}
		
		return ResultEntity.successWithData(stringValue);
	}
	
	@RequestMapping("/save/normal/string/key/value")
	public ResultEntity<String> saveNormalStringKeyValue(@RequestParam("normalKey") String normalKey, @RequestParam("normalValue") String normalValue) {
		
		try {
			redisTemplate.opsForValue().set(normalKey, normalValue);
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResultEntity.failed(e.getMessage());
		}
		
		return ResultEntity.successNoData();
	}
	
	@RequestMapping("/retrieve/token/of/signed/member/remote")
	public ResultEntity<String> retrieveTokenOfSignedMemberRemote(@RequestParam("memberSignToken") String memberSignToken) {
		
		String memberId = null;
		try {
			
			memberId = redisTemplate.opsForValue().get(memberSignToken);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			return ResultEntity.failed(e.getMessage());
		}
		
		return ResultEntity.successWithData(memberId);
	}
	
	@RequestMapping("/remove/token/of/signed/member/remote")
	public ResultEntity<String> removeTokenOfSignedMemberRemote(@RequestParam("token") String tokenAsKey) {
		
		try {
			redisTemplate.delete(tokenAsKey);
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResultEntity.failed(e.getMessage());
		}
		
		return ResultEntity.successNoData();
	}
	
	@RequestMapping("/save/token/of/signed/member/remote")
	public ResultEntity<String> saveTokenOfSignedMemberRemote(@RequestParam("token") String tokenAsKey, @RequestParam("memberId") Integer memberIdAsValue) {
		
		try {
			redisTemplate.opsForValue().set(tokenAsKey, memberIdAsValue + "", 30, TimeUnit.MINUTES);
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResultEntity.failed(e.getMessage());
		}
		
		return ResultEntity.successNoData();
	}
	
	@RequestMapping("/remove/random/code/remote")
	public ResultEntity<String> removeRandomCodeRemote(@RequestParam("randomCodeKey") String randomCodeKey) {
		
		try {
			redisTemplate.delete(randomCodeKey);
		} catch (Exception e) {
			e.printStackTrace();
			
			return ResultEntity.failed(e.getMessage());
		}
		
		return ResultEntity.successNoData();
	}
	
	@RequestMapping("/retrieve/random/code/remote")
	public ResultEntity<String> retrieveRandomCodeRemote(@RequestParam("randomCodeKey") String randomCodeKey) {
		
		String randomCodeValue = null;
		
		try {
			// 1.从Redis查询数据
			randomCodeValue = redisTemplate.opsForValue().get(randomCodeKey);
		} catch (Exception e) {
			e.printStackTrace();
			
			// 2.失败返回错误信息
			return ResultEntity.failed(e.getMessage());
		}
		
		// 3.成功返回查询结果
		return ResultEntity.successWithData(randomCodeValue);
	}
	
	@RequestMapping("/save/random/code/remote")
	public ResultEntity<String> saveRandomCodeRemote(
			@RequestParam("randomCodeKey") String randomCodeKey, 
			@RequestParam("randomCodeValue") String randomCodeValue) {
		
		try {
			
			// 1.调用Redis的方法执行随机验证码的保存操作
			redisTemplate.opsForValue().set(randomCodeKey, randomCodeValue, 15, TimeUnit.MINUTES);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// 2.返回失败结果
			return ResultEntity.failed(e.getMessage());
			
		}
		
		// 3.返回成功结果
		return ResultEntity.successNoData();
	}

}
