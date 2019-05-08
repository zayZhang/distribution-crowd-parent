package com.zay.crowd.mapper;

import com.zay.crowd.entity.po.ProjectPO;
import com.zay.crowd.entity.po.ProjectPOExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectPOMapper {
	int countByExample(ProjectPOExample example);

	int deleteByExample(ProjectPOExample example);

	int deleteByPrimaryKey(Integer id);

	int insert(ProjectPO record);

	int insertSelective(ProjectPO record);

	List<ProjectPO> selectByExample(ProjectPOExample example);

	ProjectPO selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

	int updateByExample(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

	int updateByPrimaryKeySelective(ProjectPO record);

	int updateByPrimaryKey(ProjectPO record);

	// 将分类数据typeList保存进中间表
	void insertTypeIdListToInnerTable(@Param("typeIdList") List<Integer> typeIdList, @Param("projectId") Integer projectId);

	void insertTagIdListToInnerTable(@Param("tagIdList") List<Integer> tagIdList, @Param("projectId") Integer projectId);
	
	
	
}