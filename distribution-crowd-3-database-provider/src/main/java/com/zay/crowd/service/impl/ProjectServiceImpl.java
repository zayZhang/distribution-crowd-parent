package com.zay.crowd.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zay.crowd.entity.po.MemberConfirmInfoPO;
import com.zay.crowd.entity.po.MemberConfirmInfoPOExample;
import com.zay.crowd.entity.po.MemberLaunchInfoPO;
import com.zay.crowd.entity.po.MemberLaunchInfoPOExample;
import com.zay.crowd.entity.po.ProjectPO;
import com.zay.crowd.entity.vo.MemberConfirmInfoVO;
import com.zay.crowd.entity.vo.MemberLauchInfoVO;
import com.zay.crowd.entity.vo.ProjectVO;
import com.zay.crowd.entity.vo.ReturnVO;
import com.zay.crowd.mapper.MemberConfirmInfoPOMapper;
import com.zay.crowd.mapper.MemberLaunchInfoPOMapper;
import com.zay.crowd.mapper.ProjectItemPicPOMapper;
import com.zay.crowd.mapper.ProjectPOMapper;
import com.zay.crowd.mapper.ReturnPOMapper;
import com.zay.crowd.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

	@Autowired
	private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

	@Autowired
	private ProjectItemPicPOMapper projectItemPicPOMapper;

	@Autowired
	private ProjectPOMapper projectPOMapper;

	@Autowired
	private ReturnPOMapper returnPOMapper;

	@Override
	public void saveProject(ProjectVO projectVO, String memberIdStr) {

		Integer memberId = Integer.parseInt(memberIdStr);

		// 1.将项目信息封装到ProjectPO中，执行保存，拿到自增主键
		// ①创建空PO对象
		ProjectPO projectPO = new ProjectPO();

		// ②复制属性
		BeanUtils.copyProperties(projectVO, projectPO);

		// ③执行保存
		projectPOMapper.insert(projectPO);

		// ④获取自增主键
		Integer projectId = projectPO.getId();

		// 2.将分类数据typeIdList保存到中间表
		// ①获取typeIdList数据
		List<Integer> typeIdList = projectVO.getTypeIdList();

		// ②执行保存
		projectPOMapper.insertTypeIdListToInnerTable(typeIdList, projectId);

		// 3.将标签数据tagIdList保存到中间表
		List<Integer> tagIdList = projectVO.getTagIdList();
		projectPOMapper.insertTagIdListToInnerTable(tagIdList, projectId);

		// 4.将detailPicturePathList保存到t_project_item_pic表
		// ①获取detailPicturePathList
		List<String> detailPicturePathList = projectVO.getDetailPicturePathList();

		// ②执行保存
		projectItemPicPOMapper.insertDetailPicturePathList(detailPicturePathList, projectId);

		// 5.将memberLauchInfoVO转换为对应的MemberLaunchInfoPO
		// ①获取memberLauchInfoVO
		MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();

		// ②创建MemberLaunchInfoPO对象
		MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();

		// ③复制属性
		BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);

		memberLaunchInfoPO.setMemberid(memberId);

		// ④根据memberId删除旧的数据
		MemberLaunchInfoPOExample memberLaunchInfoPOExample = new MemberLaunchInfoPOExample();
		memberLaunchInfoPOExample.createCriteria().andMemberidEqualTo(memberId);
		memberLaunchInfoPOMapper.deleteByExample(memberLaunchInfoPOExample);

		// ⑤保存MemberLaunchInfoPO对象
		memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

		// 6.将returnVOList保存到t_return表
		// ①获取returnVOList
		List<ReturnVO> returnVOList = projectVO.getReturnVOList();

		// ②执行保存
		returnPOMapper.insertReturnList(projectId, returnVOList);

		// 7.将MemberConfirmInfoVO转换为MemberConfirmInfoPO保存
		MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
		MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
		BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);

		memberConfirmInfoPO.setMemberid(memberId);

		// 保存新数据前，删除旧数据
		MemberConfirmInfoPOExample memberConfirmInfoPOExample = new MemberConfirmInfoPOExample();
		memberConfirmInfoPOExample.createCriteria().andMemberidEqualTo(memberId);
		memberConfirmInfoPOMapper.deleteByExample(memberConfirmInfoPOExample);

		memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
	}

}
