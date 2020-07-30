package com.openjava.datalake.push.repository;

import com.openjava.datalake.push.domain.DlApiPushAudit;
import org.ljdp.core.spring.data.DynamicJpaRepository;

/**
 * 接口推送审计信息数据库访问层
 * @author zmk
 *
 */
public interface DlApiPushAuditRepository extends DynamicJpaRepository<DlApiPushAudit, Long>, DlApiPushAuditRepositoryCustom{
	
}
