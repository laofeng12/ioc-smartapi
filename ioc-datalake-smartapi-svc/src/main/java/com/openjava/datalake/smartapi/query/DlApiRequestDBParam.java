package com.openjava.datalake.smartapi.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author zjf
 *
 */
public class DlApiRequestDBParam extends RoDBQueryParam {
	private Long eq_requestid;//请求参数ID --主键查询
	
	private Long eq_queryid;//查询表ID = ?
	private Long eq_columnid;//字段id = ?
	
	public Long getEq_requestid() {
		return eq_requestid;
	}
	public void setEq_requestid(Long requestid) {
		this.eq_requestid = requestid;
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