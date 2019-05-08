package com.zay.crowd.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zay.crowd.api.ProjectOperationRemoteService;
import com.zay.crowd.constant.CrowdConstant;
import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.vo.MemberLauchInfoVO;
import com.zay.crowd.entity.vo.MemberSignSuccessVO;
import com.zay.crowd.util.UploadUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags="这是对类的说明，这里是webUI中的projectUIController类，对象存储服务")
@Controller
public class ProjectUIController {
	
	@Autowired
	private ProjectOperationRemoteService projectOperationRemoteService;
	
	@Value(value="${oss.project.parent.folder}")
	private String ossProjectParentFolder;

	@Value(value="${oss.endpoint}")
	private String endpoint;

	@Value(value="${oss.accessKeyId}")
	private String accessKeyId;

	@Value(value="${oss.accessKeySecret}")
	private String accessKeySecret;

	@Value(value="${oss.bucketName}")
	private String bucketName;
		
	@Value(value="${oss.bucket.domain}")
	private String bucketDomain;
	
	@ApiOperation(value="upload，这是上传图片的方法，上传到OSS对象存储")
	@ApiImplicitParams({@ApiImplicitParam(name="username",value="用户名",required=true)})
	@ResponseBody
	@RequestMapping(value="/set/imgs")
	public ResultEntity<List<String>> upload(@RequestParam(value="simplePicture")List<MultipartFile> list) throws IOException{
		List<String> fileList=new ArrayList<>();
		for (MultipartFile multiPartFile : list) {
			String mulName=multiPartFile.getOriginalFilename();
			String name=UploadUtils.generateFileName(mulName);
			String floderName=ossProjectParentFolder+"/"+UploadUtils.generateDayFolderName();
			InputStream input=multiPartFile.getInputStream();
			UploadUtils.uploadSingleFile(endpoint, accessKeyId, accessKeySecret, name, floderName, bucketName, input);
			String ossAccessPath = bucketDomain+"/"+floderName+"/"+name;
			fileList.add(ossAccessPath);
		}
		return ResultEntity.successWithData(fileList);
	}
	
	
	@ResponseBody
	@RequestMapping(value="/project/upload/simplePicture")
	public ResultEntity<List<String>> uploadSimplePicture(@RequestParam(value="simplePicture")List<MultipartFile> list) throws IOException{
		List<String> fileList=new ArrayList<>();
		if(list.size()>0) {
			for (MultipartFile multipartFile : list) {
				// 3.获取原始文件名
				String originalFilename = multipartFile.getOriginalFilename();
				
				// 4.拼上传文件名
				String fileName = UploadUtils.generateFileName(originalFilename);
				
				// 5.存储上传文件的目录名
				String folderName = ossProjectParentFolder + "/" + UploadUtils.generateDayFolderName();
				
				// 6.获取上传文件的输入流
				InputStream inputStream = multipartFile.getInputStream();
				
				// 7.执行上传
				UploadUtils.uploadSingleFile(endpoint, accessKeyId, accessKeySecret, fileName, folderName, bucketName,
						inputStream);
				
				// 8.拼接OSS访问路径
				String ossAccessPath = bucketDomain+"/"+folderName+"/"+fileName;
				
				// 9.存入list
				fileList.add(ossAccessPath);
			}
			
		}
		return ResultEntity.successWithData(fileList);
	}
	
	@ResponseBody
	@RequestMapping("/project/upload/headPicture")
	public ResultEntity<String> uploadHeadPicture(@RequestParam("headPicture") MultipartFile headPicture) throws IOException {
		
		// 1.判断上传的文件是否为空
		if (!headPicture.isEmpty()) {
			
			// 2.获取文件原始文件名
			String originalFilename = headPicture.getOriginalFilename();
			
			// 3.构造存储文件的目录名
			String folderName = ossProjectParentFolder + "/" + UploadUtils.generateDayFolderName();
			
			// 4.生成文件名
			String fileName = UploadUtils.generateFileName(originalFilename);
			
			// 5.获取上传文件提供的输入流
			InputStream inputStream = headPicture.getInputStream();
			
			// 6.执行上传
			UploadUtils.uploadSingleFile(endpoint, accessKeyId, accessKeySecret, fileName, folderName, bucketName,
					inputStream);
			
			// 7.拼装上传文件后OSS平台上访问文件的路径
			String ossAccessPath = bucketDomain+"/"+folderName+"/"+fileName;
			
			return ResultEntity.successWithData(ossAccessPath);
		}
		
		return ResultEntity.failed(CrowdConstant.MESSAGE_NOT_FILE_UPLOADED);
	}
	
	
	
	@RequestMapping("/project/agree/protocol")
	public String agreeProtocol(HttpSession session, Model model) {
		
		// 1.检查登录状态
		MemberSignSuccessVO memberSignVO = (MemberSignSuccessVO) session.getAttribute(CrowdConstant.ATTR_NAME_SESSION_SIGNED_MEMBER);
		
		if(memberSignVO == null) {
			
			model.addAttribute(CrowdConstant.ATTR_NAME_REQUEST_MESSAGE, CrowdConstant.MESSAGE_LOGIN_NEEDED);
			
			return "member_login";
		}
		
		String token = memberSignVO.getToken();
		
		// 2.调用远程方法初始化项目创建
		ResultEntity<String> resultEntity = projectOperationRemoteService.initProjectCreate(token);
		
		if(ResultEntity.FAILED.equals(resultEntity.getResult())) {
			throw new RuntimeException(resultEntity.getMessage());
		}
		
		return "redirect:/project/to/step/one/page";
	}
	
	@RequestMapping("/project/to/step/one/page")
	public String toStepOnePage(Model model, HttpSession session) {
		
		// 1.检查登录状态
		MemberSignSuccessVO memberSignVO = (MemberSignSuccessVO) session.getAttribute(CrowdConstant.ATTR_NAME_SESSION_SIGNED_MEMBER);
		
		if(memberSignVO == null) {
			
			model.addAttribute(CrowdConstant.ATTR_NAME_REQUEST_MESSAGE, CrowdConstant.MESSAGE_LOGIN_NEEDED);
			
			return "member_login";
		}
		
		String token = memberSignVO.getToken();
		
		// 2.调用远程方法执行查询
		ResultEntity<MemberLauchInfoVO> resultEntity = projectOperationRemoteService.retrieveMemberLauchInfo(token);
		
		if(ResultEntity.FAILED.equals(resultEntity.getResult())) {
			throw new RuntimeException(resultEntity.getMessage());
		}
		
		MemberLauchInfoVO memberLauchInfoVO = resultEntity.getData();
		
		// 3.将查询结果存入模型
		if(memberLauchInfoVO != null) {
			System.err.println(memberLauchInfoVO.getDescriptionDetail());
			System.err.println(memberLauchInfoVO.getDescriptionSimple());
			System.err.println(memberLauchInfoVO.getMemberSignToken());
			System.err.println(memberLauchInfoVO.getPhoneNum());
			model.addAttribute("memberLauchInfoVO", memberLauchInfoVO);
		}
		
		return "project_2_stepOne";
	}

}
