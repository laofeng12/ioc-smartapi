package com.openjava.datalake.push.service;

import com.openjava.datalake.push.domain.DlApiPushListen;
import com.openjava.datalake.push.query.DlApiPushListenDBParam;
import com.openjava.datalake.push.vo.ApiSyncRespVO;
import com.openjava.datalake.push.vo.KafkaApiSyncMessageVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 接口推送监听表业务层接口
 * @author zmk
 *
 */
public interface DlApiPushListenService {
	Page<DlApiPushListen> query(DlApiPushListenDBParam params, Pageable pageable);
	
	List<DlApiPushListen> queryDataOnly(DlApiPushListenDBParam params, Pageable pageable);
	
	DlApiPushListen get(Long id);
	
	DlApiPushListen doSave(DlApiPushListen m) throws Exception;

	/**
	 * 根据资源编码还有创建人获取监听记录
	 * @return
	 */
	DlApiPushListen findByResourceCodeAndCreateUser(String resourceCode, Long createUser);
	/**
	 * 保存监听设置
	 */
	DlApiPushListen saveListenUrl(DlApiPushListen body)throws Exception;

	/**
	 * 推送数据
	 * @param messageVO
	 */
	ApiSyncRespVO pushData(KafkaApiSyncMessageVO messageVO) throws Exception;

}
