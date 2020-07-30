package com.openjava.datalake.push.service;

import com.openjava.datalake.push.domain.DlPushListenErrorLog;
import com.openjava.datalake.push.query.DlPushListenErrorLogDBParam;
import com.openjava.datalake.push.repository.DlPushListenErrorLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 监听事件错误日志业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DlPushListenErrorLogServiceImpl implements DlPushListenErrorLogService {
	
	@Resource
	private DlPushListenErrorLogRepository dlPushListenErrorLogRepository;
	
	public Page<DlPushListenErrorLog> query(DlPushListenErrorLogDBParam params, Pageable pageable){
		Page<DlPushListenErrorLog> pageresult = dlPushListenErrorLogRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DlPushListenErrorLog> queryDataOnly(DlPushListenErrorLogDBParam params, Pageable pageable){
		return dlPushListenErrorLogRepository.queryDataOnly(params, pageable);
	}
	
	public DlPushListenErrorLog get(String id) {
		Optional<DlPushListenErrorLog> o = dlPushListenErrorLogRepository.findById(id);
		if(o.isPresent()) {
			DlPushListenErrorLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DlPushListenErrorLog："+id);
		return null;
	}
	
	public DlPushListenErrorLog doSave(DlPushListenErrorLog m) {
		return dlPushListenErrorLogRepository.save(m);
	}
	
	public void doDelete(String id) {
		dlPushListenErrorLogRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dlPushListenErrorLogRepository.deleteById(new String(items[i]));
		}
	}
}
