package com.openjava.datalake.push.repository;

import com.openjava.datalake.push.domain.DlPushListenErrorLog;
import org.ljdp.core.spring.data.DynamicJpaRepository;

/**
 * 监听事件错误日志数据库访问层
 * @author zmk
 *
 */
public interface DlPushListenErrorLogRepository extends DynamicJpaRepository<DlPushListenErrorLog, String>, DlPushListenErrorLogRepositoryCustom{
	
}
