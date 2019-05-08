package com.zay.crowd.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zay.crowd.entity.ResultEntity;

//feign指定要调用的微服务名称
@FeignClient("redis-provider")
public interface RedisOperationRemoteService {
	/**
	 * 往redis中保存验证码
	 * @param randomCodeKey
	 * @param randomCodeValue
	 * @return
	 */
	@RequestMapping("/save/random/code/remote")
	ResultEntity<String> saveRandomCodeRemote(@RequestParam("randomCodeKey") String randomCodeKey,
			@RequestParam("randomCodeValue") String randomCodeValue);
	/**
	 * 通过key获取验证码
	 * @param randomCodeKey
	 * @return
	 */
	@RequestMapping("/retrieve/random/code/remote")
	ResultEntity<String> retrieveRandomCodeRemote(@RequestParam("randomCodeKey") String randomCodeKey);
	
	/**
	 * 删除验证码
	 * @param randomCodeKey
	 * @return
	 */
	@RequestMapping("/remove/random/code/remote")
	ResultEntity<String> removeRandomCodeRemote(@RequestParam("randomCodeKey") String randomCodeKey);

	/**
	 * 保存用户令牌
	 * @param tokenAsKey
	 * @param memberIdAsValue
	 * @return
	 */
	@RequestMapping("/save/token/of/signed/member/remote")
	ResultEntity<String> saveTokenOfSignedMemberRemote(@RequestParam("token") String tokenAsKey,
			@RequestParam("memberId") Integer memberIdAsValue);
	/**
	 * 获取用户令牌和值
	 * @param memberSignToken
	 * @return
	 */
	@RequestMapping("/retrieve/token/of/signed/member/remote")
	ResultEntity<String> retrieveTokenOfSignedMemberRemote(@RequestParam("memberSignToken") String memberSignToken);

	/**
	 * 保存redis项目信息键值对
	 * @param normalKey
	 * @param normalValue
	 * @return
	 */
	@RequestMapping("/save/normal/string/key/value")
	ResultEntity<String> saveNormalStringKeyValue(@RequestParam("normalKey") String normalKey,
			@RequestParam("normalValue") String normalValue);
	
	/**
	 * 获取项目信息 图片
	 * @param normalKey
	 * @return
	 */
	@RequestMapping("/retrieve/string/value/by/string/key")
	ResultEntity<String> retrieveStringValueByStringKey(@RequestParam("normalKey") String normalKey);
	
	@RequestMapping("/remove/by/key")
	ResultEntity<String> removeByKey(@RequestParam("key") String key);

}
