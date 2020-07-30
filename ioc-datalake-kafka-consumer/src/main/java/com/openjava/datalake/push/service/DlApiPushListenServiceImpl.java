package com.openjava.datalake.push.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.admin.component.IocAuthorizationToken;
import com.openjava.datalake.common.GlobalUniqueIdConstant;
import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.common.gateway.SpDlResJwtResp;
import com.openjava.datalake.common.responseBody.SpDlResJwt;
import com.openjava.datalake.push.domain.DlApiPushListen;
import com.openjava.datalake.push.domain.RetryTime;
import com.openjava.datalake.push.query.DlApiPushListenDBParam;
import com.openjava.datalake.push.repository.DlApiPushListenRepository;
import com.openjava.datalake.push.vo.*;
import com.openjava.datalake.rescata.domain.DlRescataResource;
import com.openjava.datalake.rescata.service.DlRescataResourceService;
import com.openjava.datalake.rescata.service.GlobalUniqueIdService;
import com.openjava.datalake.util.AccessIocPaasGatewayUtils;
import com.openjava.datalake.util.secret.AESUtils;
import com.openjava.datalake.util.secret.JwtTokenUtil;
import com.openjava.datalake.util.secret.SmUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.plugin.sys.vo.UserVO;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 接口推送监听表业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DlApiPushListenServiceImpl implements DlApiPushListenService {
	
	@Resource
	private DlApiPushListenRepository dlApiPushListenRepository;
	@Resource
	private GlobalUniqueIdService globalUniqueIdService;
	@Resource
	private DlRescataResourceService dlRescataResourceService;
	@Resource
	private IocAuthorizationToken iocAuthorizationToken;
	@Resource
	private RetryTimeService retryTimeService;

	/**
	 * 监听推送线程池
	 */
	public static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);

	public Page<DlApiPushListen> query(DlApiPushListenDBParam params, Pageable pageable){
		Page<DlApiPushListen> pageresult = dlApiPushListenRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DlApiPushListen> queryDataOnly(DlApiPushListenDBParam params, Pageable pageable){
		return dlApiPushListenRepository.queryDataOnly(params, pageable);
	}
	
	public DlApiPushListen get(Long id) {
		Optional<DlApiPushListen> o = dlApiPushListenRepository.findById(id);
		if(o.isPresent()) {
			DlApiPushListen m = o.get();
			return m;
		}
		System.out.println("找不到记录DlApiPushListen："+id);
		return null;
	}

	public DlApiPushListen doSave(DlApiPushListen m) throws Exception {
		Long listenId = m.getListenId();
		if (listenId == null) {
			Long globalUniqueIdById = globalUniqueIdService.getGlobalUniqueIdById(GlobalUniqueIdConstant.GLOBAL_UNIQUE_API_PUSH_LISTEN_ID);
			m.setListenId(globalUniqueIdById);
		}
		return dlApiPushListenRepository.save(m);
	}
	/**
	 * 根据资源编码还有创建人获取监听记录
	 * @return
	 */
	public DlApiPushListen findByResourceCodeAndCreateUser(String resourceCode,Long createUser){
		return dlApiPushListenRepository.findByResourceCodeAndCreateUser(resourceCode,createUser);
	}
	/**
	 * 保存监听设置
	 */
	public DlApiPushListen saveListenUrl(DlApiPushListen body)throws Exception{
		String resourceCode = body.getResourceCode();
		Long userId = SsoContext.getUserId();
		DlRescataResource rescataResource = dlRescataResourceService.queryLatestByResourceCode(resourceCode);
		if (rescataResource==null){
			throw new APIException(-200,"消息资源编码错误");
		}
		if (!ResourceConstant.RESOURCE_DATA_PROVIDE_MODE_INTERFACE.equals(rescataResource.getSourceMode())){
			throw new APIException(-200,"非接口获取的资源目录不能设置监听地址");
		}
		DlApiPushListen dal = this.findByResourceCodeAndCreateUser(resourceCode, userId);
		UserVO userVO = (UserVO) SsoContext.getUser();
		if (dal != null){
			dal.setListenUrl(body.getListenUrl());
			dal.setModifyAccount(userVO.getUserAccount());
			dal.setModifyUser(userId);
			dal.setModifyTime(new Date());
			return this.doSave(dal);
		}else {
			dal = body;
			dal.setCreateAccount(userVO.getUserAccount());
			dal.setCreateUser(userId);
			dal.setCreateTime(new Date());
			this.doSave(dal);
			return dal;
		}
	}
	/**
	 * 推送数据
	 * @param messageVO
	 */
	@Override
	public ApiSyncRespVO pushData(KafkaApiSyncMessageVO messageVO) throws Exception {
		Long userId = messageVO.getUserId();
		String resourceCode = messageVO.getResourceCode();
		ApiSyncReceiveDataVO syncVo = messageVO.getSyncReceiveDataVO();
		String version = messageVO.getVersion();
		DlApiPushListen listen =  this.findByResourceCodeAndCreateUser(resourceCode,userId);
		ApiSyncRespVO respVO = new ApiSyncRespVO();
		if (listen == null || listen.getListenUrl()==null){
			respVO.setMessage("没设置监听地址");
			respVO.setCode(-200);
			return respVO;
		}
		String authorization = iocAuthorizationToken.generateAesToken();//获取Authorization，无用户信息调用验证
		//step1:获取网关凭证和秘钥
		Optional<SpDlResJwtResp> optional =  AccessIocPaasGatewayUtils.getSpDlResJwt(resourceCode,authorization,"-9999");
		SpDlResJwt spDlResJwt = null;
		if (optional.isPresent()){
			spDlResJwt = optional.get().getData();
		}
		if (spDlResJwt == null || StringUtils.isBlank(spDlResJwt.getKey()) || StringUtils.isBlank(spDlResJwt.getSecret()) || StringUtils.isBlank(spDlResJwt.getAesPsw()) || StringUtils.isBlank(spDlResJwt.getAesOffset())) {
			respVO.setMessage("无api访问凭证，请咨询数据湖管理员获取");
			respVO.setCode(-200);
			return respVO;
		}
		String token = JwtTokenUtil.generateToken(spDlResJwt.getKey(),spDlResJwt.getSecret());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization","Bearer "+token);
		//step2:签名
		SortedMap<String, String> parameters = new TreeMap<>();
		parameters.put("resourceCode",resourceCode);
		parameters.put("pushSequence",syncVo.getPushSequence());
		parameters.put("beginTimestamp",syncVo.getBeginTimestamp());
		parameters.put("endTImestamp",syncVo.getEndTImestamp());
		parameters.put("column", JSONObject.toJSONString(syncVo.getColumn()));
		parameters.put("data", JSONObject.toJSONString(syncVo.getData()));
		String sign =  AESUtils.encrypt(parameters,spDlResJwt.getAesPsw(),spDlResJwt.getAesOffset());
		syncVo.setSign(sign);
		//step3:构造请求参数体
		ApiSyncPushBodyVO bodyVO = new ApiSyncPushBodyVO();
		bodyVO.setResourceCode(resourceCode);
		bodyVO.setPushSequence(syncVo.getPushSequence());
		bodyVO.setBeginTimestamp(syncVo.getBeginTimestamp());
		bodyVO.setEndTImestamp(syncVo.getBeginTimestamp());
		bodyVO.setColumn(syncVo.getColumn());
		bodyVO.setData(syncVo.getData());
		ApiSyncPushDataVO apiSyncPushDataVO = new ApiSyncPushDataVO();
		apiSyncPushDataVO.setSign(sign);
		apiSyncPushDataVO.setVersion(version);
		apiSyncPushDataVO.setJsonBody(SmUtils.sm4Encrypt(JSONObject.toJSONString(bodyVO),spDlResJwt.getSmKey()));
		apiSyncPushDataVO.setSignModel("AES/CBC/PKCS5Padding");
		apiSyncPushDataVO.setJsonBodyModel("SM4");
		respVO = AccessIocPaasGatewayUtils.pushByUrl(listen.getListenUrl(),headers, JSONObject.toJSONString(apiSyncPushDataVO));
		return respVO;
	}
	/**
	 * 数据推送（有重试机制-弃用）
	 */
	private static Map<Long,Long> retryTimes= new HashedMap();//重试时间规则，key是当前次数，value是当前次数所设置的时间,单位毫秒
	private static Map<String,Long> pushSequenceMap= new HashedMap();//当前重试次数，key是唯一之别当前任务的一个标识，value是当前是第几次重试。
	private void pushDataScheduledThread(KafkaApiSyncMessageVO messageVO){
		if (retryTimes.isEmpty()){
			List<RetryTime>  list = retryTimeService.getByRetryRuleId(1l);//暂时写死
			for (RetryTime r:list) {
				retryTimes.put(r.getNumberOfTimes(),r.getTimes());
			}
		}
		String  retryKey = messageVO.getResourceId()+"-"+messageVO.getRecordSequence()+"-"+messageVO.getUserId();
		Long numberOfTimes =  pushSequenceMap.get(retryKey);
		if (numberOfTimes==null){
			pushSequenceMap.put(retryKey,0L);
			numberOfTimes = 0L;
		}
		Long finalNumberOfTimes = numberOfTimes;
		executorService.schedule(new Runnable() {
			public void run() {
				try {
					ApiSyncRespVO respVO = pushData(messageVO);
					if (respVO.getCode()!=200){
						pushSequenceMap.put(retryKey, finalNumberOfTimes +1L);
						pushDataScheduledThread(messageVO);//递归重试
					}
				}catch (Exception e){
					//此处抛出异常是本地代码异常直接结束，不作重试
					e.printStackTrace();
				}
			}
	  }, retryTimes.get(numberOfTimes), TimeUnit.MILLISECONDS);
	}

	/**
	 * 防止重复提交(弃用)
	 * @param key
	 * @return
	 */
	private synchronized boolean pushSequenceExist(String key){
		Long numberOfTimes =  pushSequenceMap.get(key);
		if (numberOfTimes!=null){
			return true;
		}
		return false;
	}

}
