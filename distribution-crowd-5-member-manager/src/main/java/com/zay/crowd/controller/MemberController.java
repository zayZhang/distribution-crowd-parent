package com.zay.crowd.controller;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zay.crowd.api.DataBaseOperationRemoteService;
import com.zay.crowd.api.RedisOperationRemoteService;
import com.zay.crowd.constant.CrowdConstant;
import com.zay.crowd.entity.ResultEntity;
import com.zay.crowd.entity.po.MemberPO;
import com.zay.crowd.entity.vo.MemberSignSuccessVO;
import com.zay.crowd.entity.vo.MemberVO;
import com.zay.crowd.util.CrowdUtils;

@RestController
public class MemberController {

	@Autowired
	private DataBaseOperationRemoteService dataBaseOperationRemoteService;
	@Autowired
	private RedisOperationRemoteService redisOperationRemoteService;

	@Value("${crowd.short.message.appCode}")
	private String appCode;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	/**
	 * 退出登录
	 * 从redis中删除token令牌
	 * @param token
	 */
	@RequestMapping(value="/log/out")
	public ResultEntity<String> logout(@RequestParam(value="token")String token) {
		return redisOperationRemoteService.removeByKey(token);
	}

	/**
	 * 登录方法
	 * 
	 * @param loginacct
	 * @param userpswd
	 * @return 登录成功后的可暴露对象
	 */
	@RequestMapping("/member/login")
	public ResultEntity<MemberSignSuccessVO> login(@RequestParam("loginacct") String loginacct,
			@RequestParam("userpswd") String userpswd) {
		// 判断是否存在这个登陆名的数据
		ResultEntity<MemberPO> bd = dataBaseOperationRemoteService.retrieveMemberByLoginAcct(loginacct);
		if (ResultEntity.FAILED.equals(bd.getResult())) {
			return ResultEntity.failed(bd.getMessage());
		}
		MemberPO data = bd.getData();
		if (data == null) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		boolean matches = passwordEncoder.matches(userpswd, data.getUserpswd());
		if (!matches) {
			return ResultEntity.failed("密码错误" + data.getUserpswd());
		}
		// 4.生成token值
		String generateToken = CrowdUtils.generateToken();
		// 5.将token和memberId组成的键值对存入Redis
		Integer memberId = data.getId();

		ResultEntity<String> saveRedis = redisOperationRemoteService.saveTokenOfSignedMemberRemote(generateToken,
				memberId);
		if (ResultEntity.FAILED.equals(saveRedis.getResult())) {
			return ResultEntity.failed(saveRedis.getMessage());
		}

		MemberSignSuccessVO memberSignSuccessVO = new MemberSignSuccessVO();
		memberSignSuccessVO.setToken(generateToken);
		BeanUtils.copyProperties(data, memberSignSuccessVO);

		return ResultEntity.successWithData(memberSignSuccessVO);
	}
	/**
	 * 注册用户
	 * @param memberVO
	 * @return
	 */
	@RequestMapping("/member/register")
	public ResultEntity<String> register(@RequestBody MemberVO memberVO) {
		// 1.获取验证码数据并进行有效性检测
		String randomCode = memberVO.getRandomCode();

		if (!CrowdUtils.strEffectiveCheck(randomCode)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_RANDOM_CODE_INVALID);
		}

		// 2.获取手机号数据并进行有效性检测
		String phoneNum = memberVO.getPhoneNum();

		if (!CrowdUtils.strEffectiveCheck(phoneNum)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_PHONENUM_INVALID);
		}

		// 3.拼接Redis存储验证码的KEY
		String randomCodeKey = CrowdConstant.REDIS_RANDOM_CODE_PREFIX + phoneNum;

		// 4.远程调用redis-provider的方法查询对应验证码
		ResultEntity<String> randomCodeRemoteResultEntity = redisOperationRemoteService
				.retrieveRandomCodeRemote(randomCodeKey);

		if (ResultEntity.FAILED.equals(randomCodeRemoteResultEntity.getResult())) {
			return randomCodeRemoteResultEntity;
		}

		// 5.检查远程获取的验证码是否存在
		String randomCodeRemote = randomCodeRemoteResultEntity.getData();

		if (!CrowdUtils.strEffectiveCheck(randomCodeRemote)) {

			return ResultEntity.failed(CrowdConstant.MESSAGE_RANDOM_CODE_OUT_OF_DATE);

		}

		// 6.将“表单验证码”和“Redis验证码”进行比较

		if (!Objects.equals(randomCode, randomCodeRemote)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_RANDOM_CODE_NOT_MATCH);
		}
		// 7.检测登录账号是否被占用
		String loginacct = memberVO.getLoginacct();
		ResultEntity<Integer> loignAcctCountResultEntity = dataBaseOperationRemoteService
				.retrieveLoignAcctCount(loginacct);

		if (ResultEntity.FAILED.equals(loignAcctCountResultEntity.getResult())) {
			return randomCodeRemoteResultEntity;
		}

		Integer loignAcctCount = loignAcctCountResultEntity.getData();

		if (loignAcctCount > 0) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
		}

		// 8.加密
		String userpswd = memberVO.getUserpswd();

		if (!CrowdUtils.strEffectiveCheck(userpswd)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_PASSWORD_INVALID);
		}

		userpswd = passwordEncoder.encode(userpswd);
		memberVO.setUserpswd(userpswd);

		// 9.将VO对象转换为PO对象
		MemberPO memberPO = new MemberPO();
		BeanUtils.copyProperties(memberVO, memberPO);

		// 10.执行保存操作
		ResultEntity<String> saveMemberRemoteResultEntity = dataBaseOperationRemoteService.saveMemberRemote(memberPO);

		return saveMemberRemoteResultEntity;
	}

	@RequestMapping("/member/send/code")
	public ResultEntity<String> sendCode(String phoneNum) {
		if (!CrowdUtils.strEffectiveCheck(phoneNum)) {
			return ResultEntity.failed(CrowdConstant.MESSAGE_PHONENUM_INVALID);
		}
		String randomCodeValue = CrowdUtils.randomCode(4);
		CrowdUtils.sendShortMessage(appCode, randomCodeValue, phoneNum);
		String randomCodeKey = CrowdConstant.REDIS_RANDOM_CODE_PREFIX + phoneNum;
		return redisOperationRemoteService.saveRandomCodeRemote(randomCodeKey, randomCodeValue);
	}

}
