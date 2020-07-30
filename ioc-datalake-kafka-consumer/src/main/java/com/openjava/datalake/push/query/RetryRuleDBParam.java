package com.openjava.datalake.push.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author zmk
 *
 */
public class RetryRuleDBParam extends RoDBQueryParam {
	private Long eq_retryRuleId;//重试规则id --主键查询
	
	private Long eq_enable;//是否启用 = ?
	
	public Long getEq_retryRuleId() {
		return eq_retryRuleId;
	}
	public void setEq_retryRuleId(Long retryRuleId) {
		this.eq_retryRuleId = retryRuleId;
	}
	
	public Long getEq_enable() {
		return eq_enable;
	}
	public void setEq_enable(Long enable) {
		this.eq_enable = enable;
	}
}