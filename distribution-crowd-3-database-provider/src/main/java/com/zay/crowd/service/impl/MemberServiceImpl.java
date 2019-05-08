package com.zay.crowd.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zay.crowd.entity.po.MemberLaunchInfoPO;
import com.zay.crowd.entity.po.MemberLaunchInfoPOExample;
import com.zay.crowd.entity.po.MemberPO;
import com.zay.crowd.entity.po.MemberPOExample;
import com.zay.crowd.entity.vo.MemberLauchInfoVO;
import com.zay.crowd.mapper.MemberLaunchInfoPOMapper;
import com.zay.crowd.mapper.MemberPOMapper;
import com.zay.crowd.service.MemberService;
import com.zay.crowd.util.CrowdUtils;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberPOMapper memberPOMapper;
	
	@Autowired
	private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;
	
	@Override
	public Integer getLoignAcctCount(String loginAcct) {
		MemberPOExample example=new MemberPOExample();
		example.createCriteria().andLoginacctEqualTo(loginAcct);
		return memberPOMapper.countByExample(example);
	}

	@Override
	public void saveMemberPO(MemberPO memberPO) {
		memberPOMapper.insertSelective(memberPO);
	}

	@Override
	public MemberPO getMemberByLoginAcct(String loginAcct) {
		MemberPOExample example=new MemberPOExample();
		example.createCriteria().andLoginacctEqualTo(loginAcct);
		List<MemberPO> list = memberPOMapper.selectByExample(example);
		if(CrowdUtils.collectionEffectiveCheck(list)) {
			return list.get(0);
		}
		
		return null;
	}
	
	@Override
	public MemberLauchInfoVO getMemberLauchInfo(String memberId) {
		
		// 1.封装查询条件
		MemberLaunchInfoPOExample example = new MemberLaunchInfoPOExample();
		
		example.createCriteria().andMemberidEqualTo(new Integer(memberId));
		
		// 2.执行查询
		List<MemberLaunchInfoPO> list = memberLaunchInfoPOMapper.selectByExample(example);
		
		if(CrowdUtils.collectionEffectiveCheck(list)) {
			
			MemberLaunchInfoPO memberLaunchInfoPO = list.get(0);
			
			MemberLauchInfoVO memberLauchInfoVO = new MemberLauchInfoVO();
			
			// 3.复制属性
			BeanUtils.copyProperties(memberLaunchInfoPO, memberLauchInfoVO);
			
			return memberLauchInfoVO;
		}
		
		return null;
	}

}
