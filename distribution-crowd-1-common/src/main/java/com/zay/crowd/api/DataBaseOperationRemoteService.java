package com.zay.crowd.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.po.MemberPO;
import com.zay.crowd.entity.vo.MemberLauchInfoVO;
import com.zay.crowd.entity.vo.ProjectVO;

@FeignClient(value = "database-provider")
public interface DataBaseOperationRemoteService {
	@RequestMapping("/retrieve/loign/acct/count")
	ResultEntity<Integer> retrieveLoignAcctCount(@RequestParam("loginAcct") String loginAcct);

	@RequestMapping("/save/member/remote")
	ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO);

	@RequestMapping("/retrieve/member/by/login/acct")
	ResultEntity<MemberPO> retrieveMemberByLoginAcct(@RequestParam("loginAcct") String loginAcct);
	
	@RequestMapping("/persist/project/remote/{memberId}")
	ResultEntity<String> persistProjectRemote(
			@RequestBody ProjectVO projectVO, 
			@PathVariable("memberId") String memberId);

	@RequestMapping("/get/member/lauch/info")
	ResultEntity<MemberLauchInfoVO> getMemberLauchInfo(@RequestParam("memberId") String memberId);
}
