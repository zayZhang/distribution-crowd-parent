package com.zay.crowd.constant;

public class CrowdConstant {

	public static final int RANDOM_CODE_LENGTH = 4;

	public static final String REDIS_RANDOM_CODE_PREFIX = "RANDOM_CODE_";

	public static final String REDIS_MEMBER_SIGN_TOKEN_PREFIX = "SIGNED_MEMBER_";

	public static final String REDIS_PROJECT_TEMP_TOKEN_PREFIX = "PROJECT_TEMP_TOKEN_";

	public static final String MESSAGE_RANDOM_CODE_LENGTH_INVALID = "请使用正确的验证码长度";

	public static final String MESSAGE_PHONENUM_INVALID = "请输入有效手机号！";

	public static final String MESSAGE_RANDOM_CODE_INVALID = "验证码无效！";

	public static final String MESSAGE_RANDOM_CODE_OUT_OF_DATE = "验证码已过期，请重新发送短信！";

	public static final String MESSAGE_RANDOM_CODE_NOT_MATCH = "您输入的验证码不正确！";

	public static final String MESSAGE_LOGIN_ACCT_ALREADY_IN_USE = "登录账号已经被使用！请重新输入！";

	public static final String MESSAGE_PASSWORD_INVALID = "密码不符合要求！请重新输入！";

	public static final String MESSAGE_LOGIN_FAILED = "登录失败！请核对账号、密码重新输入！";

	public static final String MESSAGE_LOGIN_NEEDED = "抱歉！您已经离开了登录状态！请重新登录后再操作！";

	public static final String MESSAGE_PROJECT_NOT_FOUND_FROM_CACHE = "没有从缓存中查询到项目信息！请联系管理员！";

	public static final String ATTR_NAME_REQUEST_MESSAGE = "MESSAGE";

	public static final String ATTR_NAME_SESSION_SIGNED_MEMBER = "SIGNED_MEMBER";

	public static final String MESSAGE_NOT_FILE_UPLOADED = "图片上传失败啦！";
}
