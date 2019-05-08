package com.zay.crowd.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zay.crowd.api.MemberOperationRemoteService;
import com.zay.crowd.constant.CrowdConstant;
import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.vo.MemberSignSuccessVO;


@Controller
public class MemberUIController {
	
	@Autowired
	private MemberOperationRemoteService memberOperationRemoteService;
	
	@RequestMapping("/member/logout")
	public String logout(HttpSession session) {
	
		MemberSignSuccessVO memberSignSuccessVO = (MemberSignSuccessVO) session.getAttribute(CrowdConstant.ATTR_NAME_SESSION_SIGNED_MEMBER);
		
		if(memberSignSuccessVO  == null) {
			return "redirect:/";
		}
		
		String token = memberSignSuccessVO.getToken();
		
		memberOperationRemoteService.logout(token);
		
		session.invalidate();
		
		// templates目录下的内容不能直接访问，/index.html或/index都不行
		return "redirect:/";
	}
	
	@RequestMapping("/member/do/login")
	public String doLogin(
				@RequestParam("loginacct") String loginacct,
				@RequestParam("userpswd") String userpswd,
				Model model,
				HttpSession session
			) {
		
		// 1.调用远程方法执行登录功能
		ResultEntity<MemberSignSuccessVO> resultEntity = memberOperationRemoteService.login(loginacct, userpswd);
		
		// 2.判断执行结果是否成功
		if(ResultEntity.FAILED.equals(resultEntity.getResult())) {
			
			// 3.如果执行失败，则将失败消息存入请求域
			model.addAttribute(CrowdConstant.ATTR_NAME_REQUEST_MESSAGE, resultEntity.getMessage());
			
			// 4.回到登录页面显示提示消息
			return "member_login";
		}
		
		// 5.如果登录成功则获取MemberSignSuccessVO对象
		MemberSignSuccessVO memberSignSuccessVO = resultEntity.getData();
		
		// 6.将memberSignSuccessVO存入Session域
		session.setAttribute(CrowdConstant.ATTR_NAME_SESSION_SIGNED_MEMBER, memberSignSuccessVO);
		
		// 无法跳转，templates目录下只有index.html这样的欢迎页可以直接访问，其他页面不行
		// return "redirect:/member_center.html";
		return "redirect:/member/to/member/center/page";
	}

}
