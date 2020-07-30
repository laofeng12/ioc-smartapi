package com.openjava.datalake.push.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author zmk
 *
 */
public class EventProcessDBParam extends RoDBQueryParam {
	private Long eq_processId;//队列id --主键查询
	
	private Long eq_status;//事件状态（dl.eventprocess.status：0，待发布，1:已发布） = ?
	private Long eq_btype;//业务类型(dl.eventprocess.btype：1:api接口入库；2：监听推送数据.......) = ?
	private String eq_recordSequences;//事件唯一序列号 = ?
	private String eq_bid;//业务ID = ?
	private Long eq_retryTimes;//当前重试次数 = ?
	
	public Long getEq_processId() {
		return eq_processId;
	}
	public void setEq_processId(Long processId) {
		this.eq_processId = processId;
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
	public String getEq_recordSequences() {
		return eq_recordSequences;
	}
	public void setEq_recordSequences(String recordSequences) {
		this.eq_recordSequences = recordSequences;
	}
	public String getEq_bid() {
		return eq_bid;
	}
	public void setEq_bid(String bid) {
		this.eq_bid = bid;
	}
	public Long getEq_retryTimes() {
		return eq_retryTimes;
	}
	public void setEq_retryTimes(Long retryTimes) {
		this.eq_retryTimes = retryTimes;
	}
}