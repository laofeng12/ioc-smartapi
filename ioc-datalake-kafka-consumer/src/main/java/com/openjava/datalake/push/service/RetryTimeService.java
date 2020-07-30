package com.openjava.datalake.push.service;

import com.openjava.datalake.push.domain.RetryTime;
import com.openjava.datalake.push.query.RetryTimeDBParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 重试时间表业务层接口
 * @author zmk
 *
 */
public interface RetryTimeService {
	Page<RetryTime> query(RetryTimeDBParam params, Pageable pageable);
	
	List<RetryTime> queryDataOnly(RetryTimeDBParam params, Pageable pageable);
	
	RetryTime get(Long id);
	
	RetryTime doSave(RetryTime m);
	
	void doDelete(Long id);
	void doRemove(String ids);

	List<RetryTime> getByRetryRuleId(Long retryRuleId);
}
