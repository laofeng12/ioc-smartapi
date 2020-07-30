package com.openjava.datalake.push.service;

import com.openjava.datalake.push.domain.DlApiPushAudit;

/**
 * 接口推送审计信息业务层接口
 * @author zmk
 *
 */
public interface DlApiPushAuditService {

	DlApiPushAudit get(Long id);
	
	DlApiPushAudit doSave(DlApiPushAudit m)throws Exception;
	
}
