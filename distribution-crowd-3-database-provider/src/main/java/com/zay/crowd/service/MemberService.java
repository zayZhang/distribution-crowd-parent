package com.zay.crowd.service;

import com.zay.crowd.entity.po.MemberPO;
import com.zay.crowd.entity.vo.MemberLauchInfoVO;

public interface MemberService {

	Integer getLoignAcctCount(String loginAcct);

	void saveMemberPO(MemberPO memberPO);

	MemberPO getMemberByLoginAcct(String loginAcct);

	MemberLauchInfoVO getMemberLauchInfo(String memberId);
}
