package com.openjava.datalake.smartapi.service;

import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datalake.common.dto.ApiQueryRegUserInfo;
import com.openjava.datalake.common.gateway.BaseResp;
import com.openjava.datalake.common.gateway.GatewayPublishReq;
import com.openjava.datalake.common.gateway.GatewayRegApiReq;
import com.openjava.datalake.common.gateway.GatewayUnpublishReq;
import com.openjava.datalake.smartapi.domain.DlApiQuery;
import com.openjava.datalake.smartapi.domain.DlApiRequest;
import com.openjava.datalake.smartapi.domain.DlApiResponse;
import com.openjava.datalake.smartapi.query.DlApiQueryDBParam;
import com.openjava.datalake.smartapi.vo.ApiTableDataPageVo;
import com.openjava.datalake.smartapi.vo.GetApiDataParamVo;
import org.ljdp.component.exception.APIException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * DL_API_QUERY业务层接口
 *
 * @author zjf
 */
public interface DlApiQueryService {

	ApiTableDataPageVo getApiData(String apiCode, Map<String, Object> paramData, OaUserVO oaUserVO, GetApiDataParamVo getApiDataParamVo) throws APIException;


}
