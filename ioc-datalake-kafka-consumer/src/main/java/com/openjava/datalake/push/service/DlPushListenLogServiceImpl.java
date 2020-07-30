package com.openjava.datalake.push.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.push.dao.DlPushListenErrorLogMapper;
import com.openjava.datalake.push.domain.DlPushListenErrorLog;
import com.openjava.datalake.push.domain.DlPushListenLog;
import com.openjava.datalake.push.query.DlPushListenLogDBParam;
import com.openjava.datalake.push.repository.DlPushListenLogRepository;
import com.openjava.datalake.push.vo.ApiSyncRespVO;
import com.openjava.datalake.push.vo.KafkaApiSyncMessageVO;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 监听事件日志记录业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DlPushListenLogServiceImpl implements DlPushListenLogService {
	
	@Resource
	private DlPushListenLogRepository dlPushListenLogRepository;
	@Resource
	private SysCodeService sysCodeService;
	@Resource
	private DlApiPushListenService dlApiPushListenService;
	@Resource
	private DlPushListenErrorLogService dlPushListenErrorLogService;
	@Resource
	private DlPushListenErrorLogMapper dlPushListenErrorLogMapper;

	public Page<DlPushListenLog> query(DlPushListenLogDBParam params, Pageable pageable){
		Page<DlPushListenLog> pageresult = dlPushListenLogRepository.query(params, pageable);
		Map<String, SysCode> dleventprocesslogstatus = sysCodeService.getCodeMap("dl.eventprocess.log.status");
		for (DlPushListenLog m : pageresult.getContent()) {
			if(m.getStates() != null) {
				SysCode c = dleventprocesslogstatus.get(m.getStates().toString());
				if(c != null) {
					m.setStatesName(c.getCodename());
				}
			}
		}
		return pageresult;
	}
	
	public List<DlPushListenLog> queryDataOnly(DlPushListenLogDBParam params, Pageable pageable){
		return dlPushListenLogRepository.queryDataOnly(params, pageable);
	}
	
	public DlPushListenLog get(String id) {
		Optional<DlPushListenLog> o = dlPushListenLogRepository.findById(id);
		if(o.isPresent()) {
			DlPushListenLog m = o.get();
			if(m.getStates() != null) {
				Map<String, SysCode> dleventprocesslogstatus = sysCodeService.getCodeMap("dl.eventprocess.log.status");
				SysCode c = dleventprocesslogstatus.get(m.getStates().toString());
				if(c != null) {				
					m.setStatesName(c.getCodename());
				}
			}
			return m;
		}
		System.out.println("找不到记录DlPushListenLog："+id);
		return null;
	}
	
	public DlPushListenLog doSave(DlPushListenLog m) {
		return dlPushListenLogRepository.save(m);
	}
	
	public void doDelete(String id) {
		dlPushListenLogRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dlPushListenLogRepository.deleteById(items[i]);
		}
	}
	public List<DlPushListenLog> getNobegin(){
		return dlPushListenLogRepository.getNobegin();
	}
	@Override
	public void pushLostenJob() {
		//step1 获取需要推送和重试的记录
		List<DlPushListenLog> nobegins = getNobegin();
		//step2 遍历去推送
		for (DlPushListenLog listenLog:nobegins) {
			KafkaApiSyncMessageVO messageVO = JSONObject.parseObject(listenLog.getJsonBody(),KafkaApiSyncMessageVO.class);
			ApiSyncRespVO respVO = null;
			Date now = new Date();
			try {
				//step3 推送
				respVO = dlApiPushListenService.pushData(messageVO);
			} catch (Exception e) {
				//后台报错日志：一般不会走这里
				e.printStackTrace();
				//后台报错之-记录主表情况
				listenLog.setStates(PublicConstant.DL_EVENT_PROCESS_LOG_ERROR);
				if (listenLog.getRetryTimes()!=null){
					listenLog.setRetryTimes(listenLog.getRetryTimes()+1);
				}else {
					listenLog.setRetryTimes(1L);
				}
				listenLog.setUpdateTime(now);
				doSave(listenLog);
				//后台报错之-记录当次的错误日志
				DlPushListenErrorLog errorLog = new DlPushListenErrorLog();
				errorLog.setCreateTime(now);
				errorLog.setListenLogId(listenLog.getListenLogId());
				errorLog.setMessage(e.getMessage());
				errorLog.setRetryTimes(listenLog.getRetryTimes()-1);
				dlPushListenErrorLogService.doSave(errorLog);
			}
			//推送成功与否与其对应的处理方式
			if (respVO!=null && respVO.getCode()==200){
				//成功时-记录主表状态
				listenLog.setStates(PublicConstant.DL_EVENT_PROCESS_LOG_SUCCESS);
				listenLog.setUpdateTime(now);
				doSave(listenLog);
			}else {
				//失败时，记录主表状态
				listenLog.setStates(PublicConstant.DL_EVENT_PROCESS_LOG_ERROR);

				if (listenLog.getRetryTimes()!=null){
					listenLog.setRetryTimes(listenLog.getRetryTimes()+1);
				}else {
					listenLog.setRetryTimes(1L);
				}
				listenLog.setUpdateTime(now);
				doSave(listenLog);
				//失败时，添加当次的报错日志
				DlPushListenErrorLog errorLog = new DlPushListenErrorLog();
				errorLog.setCreateTime(new Date());
				errorLog.setListenLogId(listenLog.getListenLogId());
				errorLog.setMessage(respVO.getMessage());
				errorLog.setRetryTimes(listenLog.getRetryTimes()-1);
				dlPushListenErrorLogMapper.insert(errorLog);
//				throw new RuntimeException("失败");//测试双数据源是否会回滚
			}

		}
	}
}
