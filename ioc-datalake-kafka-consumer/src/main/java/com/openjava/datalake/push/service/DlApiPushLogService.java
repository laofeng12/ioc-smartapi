package com.openjava.datalake.push.service;

import com.openjava.datalake.push.domain.DlApiPushLog;

/**
 * 推送数据日志业务层接口
 * @author xjd
 *
 */
public interface DlApiPushLogService {

	DlApiPushLog get(String id);

	DlApiPushLog doSave(DlApiPushLog m) throws RuntimeException;

	/**
	 * 更新同步结果
	 * @param recordSequence
	 * @param code
	 * @param message
	 * @return
	 */
	DlApiPushLog updateApiPushLog(String recordSequence, Long code, String message) throws RuntimeException;
}
