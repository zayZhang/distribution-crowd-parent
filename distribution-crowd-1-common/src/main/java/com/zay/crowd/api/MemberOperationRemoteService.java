package com.zay.crowd.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.vo.MemberSignSuccessVO;
import com.zay.crowd.entity.vo.MemberVO;

@FeignClient(value="member-manager")
public interface MemberOperationRemoteService {

	@RequestMapping("/member/login")
	public ResultEntity<MemberSignSuccessVO> login(@RequestParam("loginacct") String loginacct,
			@RequestParam("userpswd") String userpswd);

	@RequestMapping("/member/register")
	public ResultEntity<String> register(@RequestBody MemberVO memberVO);

	@RequestMapping("/member/send/code")
	public ResultEntity<String> sendCode(String phoneNum);

	@RequestMapping(value="/log/out")
	public ResultEntity<String> logout(@RequestParam(value="token")String token);
}
