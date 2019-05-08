package com.zay.crowd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.po.MemberPO;
import com.zay.crowd.entity.po.ProjectPO;
import com.zay.crowd.entity.vo.MemberLauchInfoVO;
import com.zay.crowd.service.MemberService;
import com.zay.crowd.service.ProjectService;

@RestController
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/get/member/lauch/info")
	public ResultEntity<MemberLauchInfoVO> getMemberLauchInfo(@RequestParam("memberId") String memberId) {
		
		MemberLauchInfoVO memberLauchInfoVO = memberService.getMemberLauchInfo(memberId);
		
		return ResultEntity.successWithData(memberLauchInfoVO);
	}
	
	
	@RequestMapping("/retrieve/member/by/login/acct")
	ResultEntity<MemberPO> retrieveMemberByLoginAcct(@RequestParam("loginAcct") String loginAcct){
		MemberPO memberPo=null;
		try {
			memberPo=memberService.getMemberByLoginAcct(loginAcct);
		}catch(Exception e) {
			e.printStackTrace();
			return ResultEntity.failed(e.getMessage());
		}
		return ResultEntity.successWithData(memberPo);
	}

	/**
	 * 查询数据库中符合该用户名的数据有多少条
	 */
	@RequestMapping("/retrieve/loign/acct/count")
	public ResultEntity<Integer> retrieveLoignAcctCount(@RequestParam("loginAcct") String loginAcct) {

		Integer count = null;
		try {
			count = memberService.getLoignAcctCount(loginAcct);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.failed(e.getMessage());
		}
		return ResultEntity.successWithData(count);
	}

	/**
	 * 往数据库中保存用户数据
	 */
	@RequestMapping("/save/member/remote")
	public ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO) {
		try {
			memberService.saveMemberPO(memberPO);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.failed(e.getMessage());
		}
		return ResultEntity.successNoData();
	}
}
