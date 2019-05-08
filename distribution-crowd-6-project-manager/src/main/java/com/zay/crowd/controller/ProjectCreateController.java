package com.zay.crowd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.zay.crowd.api.DataBaseOperationRemoteService;
import com.zay.crowd.api.RedisOperationRemoteService;
import com.zay.crowd.constant.CrowdConstant;
import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.vo.MemberConfirmInfoVO;
import com.zay.crowd.entity.vo.MemberLauchInfoVO;
import com.zay.crowd.entity.vo.ProjectVO;
import com.zay.crowd.entity.vo.ReturnVO;
import com.zay.crowd.util.CrowdUtils;

@RestController
public class ProjectCreateController {

	@Autowired
	private RedisOperationRemoteService operationRemoteService;
	@Autowired
	private DataBaseOperationRemoteService dataBaseOperationRemoteService;
	
	@RequestMapping(value="/project/upload/headPicture")
	public ResultEntity<String> projectUploadHeadPicture(@RequestParam(value="headPicture")MultipartFile multipartFile){
		
		
		return null;
	}
	
	@RequestMapping("/retrieve/member/lauch/info")
	ResultEntity<MemberLauchInfoVO> retrieveMemberLauchInfo(@RequestParam("token") String token){
ResultEntity<String> redisResultEntity = operationRemoteService.retrieveStringValueByStringKey(token);
		
		if(ResultEntity.FAILED.equals(redisResultEntity.getResult())) {
			return ResultEntity.failed(redisResultEntity.getMessage());
		}
		
		String memberId = redisResultEntity.getData();
		
		//根据memberId 去数据库中查询对应的对象返回(详细介绍，简单介绍，客服电话等)
		ResultEntity<MemberLauchInfoVO> databaseResultEntity = dataBaseOperationRemoteService.getMemberLauchInfo(memberId);
		
		if(ResultEntity.FAILED.equals(databaseResultEntity.getResult())) {
			return ResultEntity.failed(databaseResultEntity.getMessage());
		}
		
		return databaseResultEntity;
	}
	
	@RequestMapping("/save/all/when/submit")
	public ResultEntity<String> saveAllWhenSubmit(
			@RequestParam("memberSignToken") String memberSignToken, 
			@RequestParam("projectTempToken") String projectTempToken) {
		
		ResultEntity<String> retrieveTokenResultEntity = operationRemoteService.retrieveTokenOfSignedMemberRemote(memberSignToken);
		
		if(ResultEntity.FAILED.equals(retrieveTokenResultEntity.getResult())) {
			return ResultEntity.failed(retrieveTokenResultEntity.getMessage());
		}
		
		String memberId = retrieveTokenResultEntity.getData();
		
		if(memberId == null) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_NEEDED);
		}
		
		ResultEntity<String> retrieveStringValueResultEntity = operationRemoteService.retrieveStringValueByStringKey(projectTempToken);
		
		if(ResultEntity.FAILED.equals(retrieveStringValueResultEntity.getResult())) {
			return retrieveStringValueResultEntity;
		}
		
		String json = retrieveStringValueResultEntity.getData();
		
		if(!CrowdUtils.strEffectiveCheck(json)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_PROJECT_NOT_FOUND_FROM_CACHE);
		}
		
		// 1.从Redis查询ProjectVO对象
		ProjectVO projectVO = JSON.parseObject(json, ProjectVO.class);
		
		// 2.将ProjectVO对象发送给database-provider保存
		// 将memberId传给database-provider就能够让database-provider不必查Redis
		ResultEntity<String> persistProjectResultEntity = dataBaseOperationRemoteService.persistProjectRemote(projectVO, memberId);
		
		if(ResultEntity.FAILED.equals(persistProjectResultEntity.getResult())) {
			return persistProjectResultEntity;
		}
		
		// 3.将临时存储的ProjectVO对象从Redis删除
		return operationRemoteService.removeByKey(projectTempToken);
	}
	
	/**
	 * 保存确认信息
	 * @param memberConfirmInfoVO
	 * @return
	 */
	@RequestMapping("/save/member/confirm/info/vo")
	public ResultEntity<String> saveMemberConfirmInfoVO(@RequestBody MemberConfirmInfoVO memberConfirmInfoVO) {
		
		// 1.检查memberSignToken确认用户是否登录
		String memberSignToken = memberConfirmInfoVO.getMemberSignToken();
		
		ResultEntity<String> retrieveTokenResultEntity = operationRemoteService.retrieveTokenOfSignedMemberRemote(memberSignToken);
		
		if(ResultEntity.FAILED.equals(retrieveTokenResultEntity.getResult())) {
			return ResultEntity.failed(retrieveTokenResultEntity.getMessage());
		}
		
		String memberId = retrieveTokenResultEntity.getData();
		
		if(memberId == null) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_NEEDED);
		}
		
		// 2.根据projectTempToken查询ProjectVO对象
		String projectTempToken = memberConfirmInfoVO.getProjectTempToken();
		
		ResultEntity<String> retrieveStringValueResultEntity = operationRemoteService.retrieveStringValueByStringKey(projectTempToken);

		if(ResultEntity.FAILED.equals(retrieveStringValueResultEntity.getResult())) {
			return retrieveStringValueResultEntity;
		}
		
		String json = retrieveStringValueResultEntity.getData();
		
		if(!CrowdUtils.strEffectiveCheck(json)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_PROJECT_NOT_FOUND_FROM_CACHE);
		}
		
		// 3.将JSON字符串还原成ProjectVO对象
		ProjectVO projectVO = JSON.parseObject(json, ProjectVO.class);
		
		// 4.存入memberConfirmInfoVO对象
		projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
		
		// 5.将新ProjectVO对象重新转换成JSON数据
		String newJsonProject = JSON.toJSONString(projectVO);
		
		// 6.存入Redis
		return operationRemoteService.saveNormalStringKeyValue(projectTempToken, newJsonProject);
	}
	
	/**
	 * 保存回报信息，包括回报图片，新建一个
	 * @param returnVO
	 * @return
	 */
	@RequestMapping("/save/return/vo")
	public ResultEntity<String> saveReturnVO(@RequestBody ReturnVO returnVO) {

		// 1.检查memberSignToken确认用户是否登录
		String memberSignToken = returnVO.getMemberSignToken();

		ResultEntity<String> retrieveTokenResultEntity = operationRemoteService
				.retrieveTokenOfSignedMemberRemote(memberSignToken);

		if (ResultEntity.FAILED.equals(retrieveTokenResultEntity.getResult())) {
			return ResultEntity.failed(retrieveTokenResultEntity.getMessage());
		}

		String memberId = retrieveTokenResultEntity.getData();

		if (memberId == null) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_NEEDED);
		}

		// 2.根据projectTempToken查询ProjectVO对象
		String projectTempToken = returnVO.getProjectTempToken();

		ResultEntity<String> retrieveStringValueResultEntity = operationRemoteService
				.retrieveStringValueByStringKey(projectTempToken);

		if (ResultEntity.FAILED.equals(retrieveStringValueResultEntity.getResult())) {
			return retrieveStringValueResultEntity;
		}

		String json = retrieveStringValueResultEntity.getData();

		if (!CrowdUtils.strEffectiveCheck(json)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_PROJECT_NOT_FOUND_FROM_CACHE);
		}

		// 3.将JSON字符串还原成ProjectVO对象
		ProjectVO projectVO = JSON.parseObject(json, ProjectVO.class);
		// 4.获取之前存储过的ReturnVO的List
		List<ReturnVO> returnVOList = projectVO.getReturnVOList();
		// 5.如果returnVOList不是有效集合，则进行初始化，并将回报信息保存进去
		if(!CrowdUtils.collectionEffectiveCheck(returnVOList)) {
			returnVOList=new ArrayList<>();
			projectVO.setReturnVOList(returnVOList);
		}
		// 6.将当前ReturnVO存入returnVOList
		returnVOList.add(returnVO);
		// 7.将ProjectVO转换为JSON字符串
		String jsonString = JSON.toJSONString(projectVO);
		// 8.存入Redis
		return operationRemoteService.saveNormalStringKeyValue(projectTempToken, jsonString);
	}

	/**
	 * 
	 * @param memberLauchInfoVO
	 * @return
	 */
	@RequestMapping("/save/member/lauch/info/vo")
	public ResultEntity<String> saveMemberLauchInfoVO(@RequestBody MemberLauchInfoVO memberLauchInfoVO) {
		
		// 1.检查memberSignToken确认用户是否登录
		String memberSignToken = memberLauchInfoVO.getMemberSignToken();
		//根据token获取memberId
		ResultEntity<String> retrieveTokenResultEntity = operationRemoteService.retrieveTokenOfSignedMemberRemote(memberSignToken);
		
		if(ResultEntity.FAILED.equals(retrieveTokenResultEntity.getResult())) {
			return ResultEntity.failed(retrieveTokenResultEntity.getMessage());
		}
		
		String memberId = retrieveTokenResultEntity.getData();
		
		if(memberId == null) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_NEEDED);
		}
		
		// 2.根据projectTempToken查询ProjectVO对象
		String projectTempToken = memberLauchInfoVO.getProjectTempToken();
		
		ResultEntity<String> retrieveStringValueResultEntity = operationRemoteService.retrieveStringValueByStringKey(projectTempToken);
	
		if(ResultEntity.FAILED.equals(retrieveStringValueResultEntity.getResult())) {
			return retrieveStringValueResultEntity;
		}
		
		String json = retrieveStringValueResultEntity.getData();
		
		if(!CrowdUtils.strEffectiveCheck(json)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_PROJECT_NOT_FOUND_FROM_CACHE);
		}
		
		// 3.将JSON字符串还原成ProjectVO对象
		ProjectVO projectVO = JSON.parseObject(json, ProjectVO.class);
		
		// 4.存入memberLauchInfoVO对象
		projectVO.setMemberLauchInfoVO(memberLauchInfoVO);
		
		// 5.将新ProjectVO对象重新转换成JSON数据
		String newJsonProject = JSON.toJSONString(projectVO);
		
		// 6.存入Redis
		return operationRemoteService.saveNormalStringKeyValue(projectTempToken, newJsonProject);
	}
	/**
	 * 保存项目信息
	 * 
	 * @param projectVOFore
	 * @return
	 */
	@RequestMapping("/save/project/info")
	public ResultEntity<String> saveProjectInfo(@RequestBody ProjectVO projectVOFore) {

		// 1.检查memberSignToken是否有效
		String memberSignToken = projectVOFore.getMemberSignToken();

		ResultEntity<String> retrieveTokenResultEntity = operationRemoteService
				.retrieveTokenOfSignedMemberRemote(memberSignToken);

		if (ResultEntity.FAILED.equals(retrieveTokenResultEntity.getResult())) {
			return ResultEntity.failed(retrieveTokenResultEntity.getMessage());
		}

		String memberId = retrieveTokenResultEntity.getData();

		if (memberId == null) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_NEEDED);
		}

		// 2.根据projectTempToken到Redis查询ProjectVO对象对应的JSON字符串
		String projectTempToken = projectVOFore.getProjectTempToken();

		ResultEntity<String> retrieveStringValueResultEntity = operationRemoteService
				.retrieveStringValueByStringKey(projectTempToken);

		if (ResultEntity.FAILED.equals(retrieveStringValueResultEntity.getResult())) {
			return retrieveStringValueResultEntity;
		}

		String json = retrieveStringValueResultEntity.getData();

		if (!CrowdUtils.strEffectiveCheck(json)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_PROJECT_NOT_FOUND_FROM_CACHE);
		}

		// 3.将JSON字符串还原成ProjectVO对象
		ProjectVO parseObject = JSON.parseObject(json, ProjectVO.class);
		// 4.将前端传过来的ProjectVO对象中的属性值拷贝到Redis中取出的ProjectVO对象中
		BeanUtils.copyProperties(projectVOFore, parseObject);
		// 5.将复制了新属性值的projectVOBehind对象转换为JSON数据
		String jsonString = JSON.toJSONString(parseObject);
		// 6.存入Redis
		return operationRemoteService.saveNormalStringKeyValue(projectTempToken, jsonString);
	}

	/**
	 * 上传项目详细图
	 *  PROJECT_TEMP_TOKEN_45121317379b4b1a8e11bf06cf3a3e56
	 * @param memberSignToken
	 * @param projectTempToken
	 * @param detailPicturePathList
	 * @return
	 */
	@RequestMapping("/save/detail/picture/path/list")
	public ResultEntity<String> saveDetailPicturePathList(@RequestParam("memberSignToken") String memberSignToken,
			@RequestParam("projectTempToken") String projectTempToken,
			@RequestParam("detailPicturePathList") List<String> detailPicturePathList) {

		// 1.检查memberSignToken是否有效
		ResultEntity<String> retrieveTokenResultEntity = operationRemoteService
				.retrieveTokenOfSignedMemberRemote(memberSignToken);

		if (ResultEntity.FAILED.equals(retrieveTokenResultEntity.getResult())) {
			return ResultEntity.failed(retrieveTokenResultEntity.getMessage());
		}

		String memberId = retrieveTokenResultEntity.getData();

		if (memberId == null) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_NEEDED);
		}

		// 2.根据projectTempToken到Redis查询ProjectVO对象对应的JSON字符串
		ResultEntity<String> retrieveStringValueResultEntity = operationRemoteService
				.retrieveStringValueByStringKey(projectTempToken);

		if (ResultEntity.FAILED.equals(retrieveStringValueResultEntity.getResult())) {
			return retrieveStringValueResultEntity;
		}

		String json = retrieveStringValueResultEntity.getData();

		// 3.将json对象还原为ProjectVO对象
		ProjectVO projectVO = JSON.parseObject(json, ProjectVO.class);

		// 4.设置headerPicturePath
		projectVO.setDetailPicturePathList(detailPicturePathList);

		// 5.再将更新了数据的ProjectVO对象转换为JSON字符串
		String newProjectVOJSONValue = JSON.toJSONString(projectVO);

		// 6.存入Redis
		return operationRemoteService.saveNormalStringKeyValue(projectTempToken, newProjectVOJSONValue);
	}

	@RequestMapping("/save/header/picture/path")
	public ResultEntity<String> saveHeaderPicturePath(@RequestParam("memberSignToken") String memberSignToken,
			@RequestParam("projectTempToken") String projectTempToken,
			@RequestParam("headerPicturePath") String headerPicturePath) {

		// 1.检查memberSignToken是否有效
		ResultEntity<String> retrieveTokenResultEntity = operationRemoteService
				.retrieveTokenOfSignedMemberRemote(memberSignToken);

		if (ResultEntity.FAILED.equals(retrieveTokenResultEntity.getResult())) {
			return ResultEntity.failed(retrieveTokenResultEntity.getMessage()+"没有这个参数"+memberSignToken);
		}

		String memberId = retrieveTokenResultEntity.getData();

		if (memberId == null) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_NEEDED);
		}

		// 2.根据projectTempToken到Redis查询ProjectVO对象对应的JSON字符串
		// 这一步是最重要的，因为要获取到之后往里面追加信息
		ResultEntity<String> retrieveStringValueResultEntity = operationRemoteService
				.retrieveStringValueByStringKey(projectTempToken);

		if (ResultEntity.FAILED.equals(retrieveStringValueResultEntity.getResult())) {
			return retrieveStringValueResultEntity;
		}

		String json = retrieveStringValueResultEntity.getData();

		// 3.将json对象还原为ProjectVO对象
		ProjectVO projectVO = JSON.parseObject(json, ProjectVO.class);

		// 4.设置headerPicturePath,每添加一个详细图片就会添加一个对应的图片路径到redis
		// 因为详细照片是list集合，所以可以不断追加
		projectVO.setHeaderPicturePath(headerPicturePath);

		// 5.再将更新了数据的ProjectVO对象转换为JSON字符串
		String newProjectVOJSONValue = JSON.toJSONString(projectVO);

		// 6.存入Redis
		return operationRemoteService.saveNormalStringKeyValue(projectTempToken, newProjectVOJSONValue);
	}

	/**
	 * 登陆时，将对应的用户的token存进redis
	 * @param memberSignToken
	 * @return
	 */
	@RequestMapping("/init/project/create")
	public ResultEntity<String> initProjectCreate(@RequestParam("memberSignToken") String memberSignToken) {

		// 1.检查memberSignToken是否有效
		ResultEntity<String> retrieveTokenResultEntity = operationRemoteService
				.retrieveTokenOfSignedMemberRemote(memberSignToken);

		if (ResultEntity.FAILED.equals(retrieveTokenResultEntity.getResult())) {
			return ResultEntity.failed(retrieveTokenResultEntity.getMessage());
		}

		String memberId = retrieveTokenResultEntity.getData();

		if (memberId == null) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_NEEDED);
		}

		// 2.创建ProjectVO对象
		ProjectVO projectVO = new ProjectVO();

		// 3.给ProjectVO对象设置memberSignToken
		projectVO.setMemberSignToken(memberSignToken);

		// 4.将ProjectVO存入Redis
		// 生成可key
		String key = CrowdConstant.REDIS_PROJECT_TEMP_TOKEN_PREFIX + UUID.randomUUID().toString().replaceAll("-", "");

		// ※REDIS_PROJECT_TEMP_TOKEN_PREFIX也需要设置到ProjectVO对象中
		projectVO.setProjectTempToken(key);

		// 生成VALUE
		String value = JSON.toJSONString(projectVO);

		// 调用远程方法执行保存操作 保存的也就是开始发起项目的信息，后面一步步的操作会往里面添加信息
		//将token值作为键随机value作为值存进redis
		return operationRemoteService.saveNormalStringKeyValue(key, value);
	}

}
