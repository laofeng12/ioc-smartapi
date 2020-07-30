package com.openjava.datalake.push.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author zmk
 *
 */
public class DlApiPushListenDBParam extends RoDBQueryParam {
	private Long eq_listenId;//主键 --主键查询
	
	private String eq_resourceCode;//信息资源编码 = ?
	private String eq_createAccount;//创建用户账号 = ?
	
	public Long getEq_listenId() {
		return eq_listenId;
	}
	public void setEq_listenId(Long listenId) {
		this.eq_listenId = listenId;
	}
	
	public String getEq_resourceCode() {
		return eq_resourceCode;
	}
	public void setEq_resourceCode(String resourceCode) {
		this.eq_resourceCode = resourceCode;
	}
	public String getEq_createAccount() {
		return eq_createAccount;
	}
	public void setEq_createAccount(String createAccount) {
		this.eq_createAccount = createAccount;
	}
}