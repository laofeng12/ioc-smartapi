package com.openjava.datalake.push.query;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 查询对象
 * @author xjd
 *
 */
public class DlApiPushLogDBParam extends RoDBQueryParam {
	private Long eq_logId;//主键 --主键查询
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_beginTimestamp;//开始时间 >= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_endTimestamp;//结束时间 <= ?
	private Long eq_pid;//引用接口管理表ID = ?
	private Long eq_resCode;//结果码 = ?
	
	public Long getEq_logId() {
		return eq_logId;
	}
	public void setEq_logId(Long logId) {
		this.eq_logId = logId;
	}
	
	public Date getGe_beginTimestamp() {
		return ge_beginTimestamp;
	}
	public void setGe_beginTimestamp(Date beginTimestamp) {
		this.ge_beginTimestamp = beginTimestamp;
	}
	public Date getLe_endTimestamp() {
		return le_endTimestamp;
	}
	public void setLe_endTimestamp(Date endTimestamp) {
		this.le_endTimestamp = endTimestamp;
	}
	public Long getEq_pid() {
		return eq_pid;
	}
	public void setEq_pid(Long pid) {
		this.eq_pid = pid;
	}
	public Long getEq_resCode() {
		return eq_resCode;
	}
	public void setEq_resCode(Long resCode) {
		this.eq_resCode = resCode;
	}
}