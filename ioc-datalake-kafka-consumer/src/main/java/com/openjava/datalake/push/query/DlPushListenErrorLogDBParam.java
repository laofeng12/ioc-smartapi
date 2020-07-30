package com.openjava.datalake.push.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author zmk
 *
 */
public class DlPushListenErrorLogDBParam extends RoDBQueryParam {
	private String eq_id;//主键 --主键查询
	
	
	public String getEq_id() {
		return eq_id;
	}
	public void setEq_id(String id) {
		this.eq_id = id;
	}
	
}