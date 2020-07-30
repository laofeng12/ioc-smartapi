package com.openjava.datalake.push.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author zmk
 *
 */
public class DlPushListenLogDBParam extends RoDBQueryParam {
	
	private String eq_recordSequences;//事件唯一序列号 = ?
	private Long eq_id;//主键 = ?
	private String eq_states;//状态 = ?
	private String eq_bid;//业务ID = ?
	
	
	public String getEq_recordSequences() {
		return eq_recordSequences;
	}
	public void setEq_recordSequences(String recordSequences) {
		this.eq_recordSequences = recordSequences;
	}
	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	public String getEq_states() {
		return eq_states;
	}
	public void setEq_states(String states) {
		this.eq_states = states;
	}
	public String getEq_bid() {
		return eq_bid;
	}
	public void setEq_bid(String bid) {
		this.eq_bid = bid;
	}
}