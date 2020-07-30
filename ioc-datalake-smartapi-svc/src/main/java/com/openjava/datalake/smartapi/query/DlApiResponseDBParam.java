package com.openjava.datalake.smartapi.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author zjf
 *
 */
public class DlApiResponseDBParam extends RoDBQueryParam {
	private Long eq_responseid;//响应参数id --主键查询
	
	private Long eq_queryid;//查询表ID = ?
	private Long eq_columnid;//字段ID = ?
	
	public Long getEq_responseid() {
		return eq_responseid;
	}
	public void setEq_responseid(Long responseid) {
		this.eq_responseid = responseid;
	}
	
	public Long getEq_queryid() {
		return eq_queryid;
	}
	public void setEq_queryid(Long queryid) {
		this.eq_queryid = queryid;
	}
	public Long getEq_columnid() {
		return eq_columnid;
	}
	public void setEq_columnid(Long columnid) {
		this.eq_columnid = columnid;
	}
}