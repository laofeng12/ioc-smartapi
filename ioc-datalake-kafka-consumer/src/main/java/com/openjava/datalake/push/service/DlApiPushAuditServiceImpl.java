package com.openjava.datalake.push.service;

import com.openjava.datalake.push.domain.DlApiPushAudit;
import com.openjava.datalake.push.repository.DlApiPushAuditRepository;
import com.openjava.util.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 接口推送审计信息业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DlApiPushAuditServiceImpl implements DlApiPushAuditService {
	
	@Resource
	private DlApiPushAuditRepository dlApiPushAuditRepository;

	public DlApiPushAudit get(Long id) {
		Optional<DlApiPushAudit> o = dlApiPushAuditRepository.findById(id);
		if(o.isPresent()) {
			DlApiPushAudit m = o.get();
			return m;
		}
		System.out.println("找不到记录DlApiPushAudit："+id);
		return null;
	}
	
	public DlApiPushAudit doSave(DlApiPushAudit m) throws Exception{
		if (m.getPushAuditId()==null) {
			Long globalUniqueIdById = IdUtils.nextId();
			m.setPushAuditId(globalUniqueIdById);
		}
		return dlApiPushAuditRepository.save(m);
	}
	
}
