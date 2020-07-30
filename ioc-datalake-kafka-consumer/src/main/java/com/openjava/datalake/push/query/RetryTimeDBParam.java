package com.openjava.datalake.push.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author zmk
 *
 */
public class RetryTimeDBParam extends RoDBQueryParam {
	private Long eq_id;//主键 --主键查询
	
	private Long eq_retryRuleId;//重试规则id = ?
	private Long eq_numberOfTimes;//当前重启次数 = ?
	
	public Long getEq_id() {
		return eq_id;
	}
	public void setEq_id(Long id) {
		this.eq_id = id;
	}
	
	public Long getEq_retryRuleId() {
		return eq_retryRuleId;
	}
	public void setEq_retryRuleId(Long retryRuleId) {
		this.eq_retryRuleId = retryRuleId;
	}
	public Long getEq_numberOfTimes() {
		return eq_numberOfTimes;
	}
	public void setEq_numberOfTimes(Long numberOfTimes) {
		this.eq_numberOfTimes = numberOfTimes;
	}
}