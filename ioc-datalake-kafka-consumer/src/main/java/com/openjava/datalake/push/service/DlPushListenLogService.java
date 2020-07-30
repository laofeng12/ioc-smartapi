package com.openjava.datalake.push.service;

import com.openjava.datalake.push.domain.DlPushListenLog;
import com.openjava.datalake.push.query.DlPushListenLogDBParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 监听事件日志记录业务层接口
 * @author zmk
 *
 */
public interface DlPushListenLogService {
	Page<DlPushListenLog> query(DlPushListenLogDBParam params, Pageable pageable);
	
	List<DlPushListenLog> queryDataOnly(DlPushListenLogDBParam params, Pageable pageable);
	
	DlPushListenLog get(String id);
	
	DlPushListenLog doSave(DlPushListenLog m);
	
	void doDelete(String id);
	void doRemove(String ids);
	List<DlPushListenLog> getNobegin();
	void pushLostenJob();
}
