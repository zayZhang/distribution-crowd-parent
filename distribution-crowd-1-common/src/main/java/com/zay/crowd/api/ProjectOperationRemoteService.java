package com.zay.crowd.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.vo.MemberConfirmInfoVO;
import com.zay.crowd.entity.vo.MemberLauchInfoVO;
import com.zay.crowd.entity.vo.ProjectVO;
import com.zay.crowd.entity.vo.ReturnVO;

@FeignClient("project-manager")
public interface ProjectOperationRemoteService {

	@RequestMapping("/save/all/when/submit")
	ResultEntity<String> saveAllWhenSubmit(@RequestParam("memberSignToken") String memberSignToken,
			@RequestParam("projectTempToken") String projectTempToken);

	@RequestMapping("/save/member/confirm/info/vo")
	ResultEntity<String> saveMemberConfirmInfoVO(@RequestBody MemberConfirmInfoVO memberConfirmInfoVO);

	@RequestMapping("/save/return/vo")
	ResultEntity<String> saveReturnVO(@RequestBody ReturnVO returnVO);

	@RequestMapping("/save/member/lauch/info/vo")
	ResultEntity<String> saveMemberLauchInfoVO(@RequestBody MemberLauchInfoVO memberLauchInfoVO);

	@RequestMapping("/save/project/info")
	ResultEntity<String> saveProjectInfo(@RequestBody ProjectVO projectVOFore);

	@RequestMapping("/save/detail/picture/path/list")
	ResultEntity<String> saveDetailPicturePathList(@RequestParam("memberSignToken") String memberSignToken,
			@RequestParam("projectTempToken") String projectTempToken,
			@RequestParam("detailPicturePathList") List<String> detailPicturePathList);

	@RequestMapping("/save/header/picture/path")
	ResultEntity<String> saveHeaderPicturePath(@RequestParam("memberSignToken") String memberSignToken,
			@RequestParam("projectTempToken") String projectTempToken,
			@RequestParam("headerPicturePath") String headerPicturePath);

	@RequestMapping("/init/project/create")
	ResultEntity<String> initProjectCreate(@RequestParam("memberSignToken") String memberSignToken);
	
	@RequestMapping("/retrieve/member/lauch/info")
	ResultEntity<MemberLauchInfoVO> retrieveMemberLauchInfo(@RequestParam("token") String token);

}
