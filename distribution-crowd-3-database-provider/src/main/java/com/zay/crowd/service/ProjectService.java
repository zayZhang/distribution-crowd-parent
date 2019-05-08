package com.zay.crowd.service;

import com.zay.crowd.entity.po.ProjectPO;
import com.zay.crowd.entity.vo.ProjectVO;

public interface ProjectService {

	void saveProject(ProjectVO projectVO, String memberId);

}
