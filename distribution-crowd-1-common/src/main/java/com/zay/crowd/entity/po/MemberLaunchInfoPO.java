package com.zay.crowd.entity.po;

/**
 * 项目及发起人信息 包含联系电话，客服电话，不包含详细图片，头图片和项目信息
 * 
 * @author zay
 *
 */
public class MemberLaunchInfoPO {
	private Integer id;

	private Integer memberid;

	private String descriptionSimple;

	private String descriptionDetail;

	private String phoneNum;

	private String serviceNum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMemberid() {
		return memberid;
	}

	public void setMemberid(Integer memberid) {
		this.memberid = memberid;
	}

	public String getDescriptionSimple() {
		return descriptionSimple;
	}

	public void setDescriptionSimple(String descriptionSimple) {
		this.descriptionSimple = descriptionSimple == null ? null : descriptionSimple.trim();
	}

	public String getDescriptionDetail() {
		return descriptionDetail;
	}

	public void setDescriptionDetail(String descriptionDetail) {
		this.descriptionDetail = descriptionDetail == null ? null : descriptionDetail.trim();
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum == null ? null : phoneNum.trim();
	}

	public String getServiceNum() {
		return serviceNum;
	}

	public void setServiceNum(String serviceNum) {
		this.serviceNum = serviceNum == null ? null : serviceNum.trim();
	}
}