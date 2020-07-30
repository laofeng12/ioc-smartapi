package com.openjava.datalake.push.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author zmk
 *
 */
public class KafkaEventpublishDBParam extends RoDBQueryParam {
	private String eq_publishId;//发布id(UUID) --主键查询
	
	private Long eq_status;//事件状态（dl.eventpublish.status：0，待发布，1:已发布） = ?
	private Long eq_btype;//业务类型(dl.eventpublish.btype 1:api接口入库事件.......) = ?
	private String eq_bid;//业务ID = ?
	private String eq_recordSequences;//事件唯一序列号 = ?
	
	public String getEq_publishId() {
		return eq_publishId;
	}
	public void setEq_publishId(String publishId) {
		this.eq_publishId = publishId;
	}
	
	public Long getEq_status() {
		return eq_status;
	}
	public void setEq_status(Long status) {
		this.eq_status = status;
	}
	public Long getEq_btype() {
		return eq_btype;
	}
	public void setEq_btype(Long btype) {
		this.eq_btype = btype;
	}
	public String getEq_bid() {
		return eq_bid;
	}
	public void setEq_bid(String bid) {
		this.eq_bid = bid;
	}
	public String getEq_recordSequences() {
		return eq_recordSequences;
	}
	public void setEq_recordSequences(String recordSequences) {
		this.eq_recordSequences = recordSequences;
	}
}