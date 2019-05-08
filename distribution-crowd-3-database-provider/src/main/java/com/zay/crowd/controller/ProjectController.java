package com.zay.crowd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.vo.ProjectVO;
import com.zay.crowd.service.ProjectService;

@RestController
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	//最后保存项目各个信息
	@RequestMapping("/persist/project/remote/{memberId}")
	public ResultEntity<String> persistProjectRemote(
			@RequestBody ProjectVO projectVO, 
			@PathVariable("memberId") String memberId) {
		try {
			projectService.saveProject(projectVO, memberId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.failed(e.getMessage()+"异常信息");
		}
		return ResultEntity.successNoData();
	}
}
