package com.openjava.datalake.push.service;

import com.openjava.datalake.push.domain.DlPushListenErrorLog;
import com.openjava.datalake.push.query.DlPushListenErrorLogDBParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 监听事件错误日志业务层接口
 * @author zmk
 *
 */
public interface DlPushListenErrorLogService {
	Page<DlPushListenErrorLog> query(DlPushListenErrorLogDBParam params, Pageable pageable);
	
	List<DlPushListenErrorLog> queryDataOnly(DlPushListenErrorLogDBParam params, Pageable pageable);
	
	DlPushListenErrorLog get(String id);
	
	DlPushListenErrorLog doSave(DlPushListenErrorLog m);
	
	void doDelete(String id);
	void doRemove(String ids);
}
