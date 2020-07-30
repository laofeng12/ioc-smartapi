package com.openjava.datalake.push.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.openjava.datalake.push.dao.RetryTimeMapper;
import com.openjava.datalake.push.domain.RetryTime;
import com.openjava.datalake.push.query.RetryTimeDBParam;
import com.openjava.datalake.push.repository.RetryTimeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 重试时间表业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class RetryTimeServiceImpl implements RetryTimeService {
	
	@Resource
	private RetryTimeRepository retryTimeRepository;
	@Resource
	private RetryTimeMapper retryTimeMapper;
	public Page<RetryTime> query(RetryTimeDBParam params, Pageable pageable){
		Page<RetryTime> pageresult = retryTimeRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<RetryTime> queryDataOnly(RetryTimeDBParam params, Pageable pageable){
		return retryTimeRepository.queryDataOnly(params, pageable);
	}
	
	public RetryTime get(Long id) {
		Optional<RetryTime> o = retryTimeRepository.findById(id);
		if(o.isPresent()) {
			RetryTime m = o.get();
			return m;
		}
		System.out.println("找不到记录RetryTime："+id);
		return null;
	}
	
	public RetryTime doSave(RetryTime m) {
		return retryTimeRepository.save(m);
	}
	
	public void doDelete(Long id) {
		retryTimeRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			retryTimeRepository.deleteById(new Long(items[i]));
		}
	}
	public List<RetryTime> getByRetryRuleId(Long retryRuleId){
		QueryWrapper<RetryTime> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("RETRY_RULE_ID", retryRuleId);
		return retryTimeMapper.selectList(queryWrapper);
	}
}
