package com.openjava.datalake.push.service;

import com.openjava.datalake.push.domain.DlApiPushLog;
import com.openjava.datalake.push.repository.DlApiPushLogRepository;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 推送数据日志业务层
 * @author xjd
 *
 */
@Service
@Transactional
public class DlApiPushLogServiceImpl implements DlApiPushLogService {

	private static Logger LOGGER = LoggerFactory.getLogger(DlApiPushLogServiceImpl.class);
	
	@Resource
	private DlApiPushLogRepository dlApiPushLogRepository;


	@Override
	public DlApiPushLog get(String id) {
		Optional<DlApiPushLog> o = dlApiPushLogRepository.findById(id);
		if(o.isPresent()) {
			DlApiPushLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DlApiPushLog："+id);
		return null;
	}

	@Override
	public DlApiPushLog doSave(DlApiPushLog m) throws RuntimeException {
		String logId = m.getRecordSequence();
		if (StringUtils.isBlank(logId)){
//			Long logGUID = globalUniqueIdService.getGlobalUniqueIdById(GlobalUniqueIdConstant.GLOBAL_UNIQUE_API_PUSH_LOG_ID);
//			if (logGUID != null) {
//				logId = String.valueOf(logGUID);
//			} else {
//			}
			SequenceService ss = ConcurrentSequence.getInstance();
			logId = String.valueOf(ss.getSequence());
		}
		m.setRecordSequence(logId);
		DlApiPushLog save = dlApiPushLogRepository.save(m);
		return save;
	}

	/**
	 * 更新同步结果
	 * @param recordSequence
	 * @param code
	 * @param message
	 * @return
	 */
	@Override
	public DlApiPushLog updateApiPushLog(String recordSequence,Long code,String message) throws RuntimeException {
		DlApiPushLog dlApiPushLog = this.get(recordSequence);
		dlApiPushLog.setResCode(code);
		dlApiPushLog.setResMessage(message);
		return this.doSave(dlApiPushLog);
	}

}
