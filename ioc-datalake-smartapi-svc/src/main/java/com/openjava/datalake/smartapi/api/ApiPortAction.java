package com.openjava.datalake.smartapi.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datalake.component.AuditComponentService;
import com.openjava.datalake.component.GateWayUtil;
import com.openjava.datalake.rescata.vo.ColumnInfoVo;
import com.openjava.datalake.smartapi.service.DlApiQueryService;
import com.openjava.datalake.smartapi.vo.ApiTableDataPageVo;
import com.openjava.datalake.smartapi.vo.GetApiDataParamVo;
import com.openjava.datalake.subscribe.domain.DlDataAccessLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * api接口
 *
 * @author zjf
 */
@Api(tags = "智能API查询数据开放接口")
@RestController
@RequestMapping("/")
public class ApiPortAction {

    @Resource
    private DlApiQueryService dlApiQueryService;

    @Autowired
    private AuditComponentService auditComponentService;

    @ApiOperation(value = "查询API接口的数据-带版本号")
    @Security(session = false)
    @RequestMapping(value = "{version}/datalake/smartapi/apiport/{apiCode}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiTableDataPageVo> doGetApiByVersion(HttpServletRequest request,
                                                                @PathVariable("version") String version,
                                                                @RequestBody(required = false) JSONObject jsonObject,
                                                                @PathVariable("apiCode") String apiCode) throws APIException {
        System.out.println("开始时间"+new Date().toString());
        String [] vs =  version.split("v");
        try {
            Long num = Long.valueOf(vs[1]);
            if (!StringUtils.isBlank(vs[0]) || !(num >=0L) || !(vs.length==2)){
                throw new APIException(400,"{API_VERSION}有误");
            }
            version = vs[1];
        }catch (Exception e){
            throw new APIException(-200,"{API_VERSION}有误");
        }
        OaUserVO oaUserVO = GateWayUtil.decodeUserInfo(request);
//        OaUserVO oaUserVO = new OaUserVO();
//        oaUserVO.setUserId("1");
//        oaUserVO.setUserAccount("datalake-sysadmin");
//        oaUserVO.setUserName("政数局-系统管理员");
        ResponseEntity<ApiTableDataPageVo>  responseEntity = this.getApiTableDataPageVo(request, jsonObject, apiCode,version,oaUserVO);
        auditComponentService.saveAuditLogForQuery("数据湖", "数据服务", "查询接口管理", "API查询", apiCode,oaUserVO.getUserAccount(),oaUserVO.getUserId());
        System.out.println("结束时间："+new Date().toString());
        return responseEntity;
    }

    @ApiOperation(value = "查询API接口的数据")
    @Security(session = true)
    @RequestMapping(value = "datalake/smartapi/interal/apiport/{apiCode}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiTableDataPageVo> doGetApi(HttpServletRequest request,
                                                       @RequestBody(required = false) JSONObject jsonObject,
                                                       @PathVariable("apiCode") String apiCode) throws APIException {
        return this.getApiTableDataPageVo(request, jsonObject, apiCode,null,null);
    }

    private ResponseEntity<ApiTableDataPageVo> getApiTableDataPageVo(HttpServletRequest request, JSONObject jsonObject, String apiCode, String version, OaUserVO userVO) throws APIException {
        DlDataAccessLog dlDataAccessLog = new DlDataAccessLog();
        Map<String, Object> paramData = new HashMap<>();
        OaUserVO oaUserVO = null;
        // 是否排除数据湖的权限校验
        boolean passDatalakeAuth = false;
        try {
            //权限校验
             oaUserVO = (OaUserVO) SsoContext.getUser();
            if (oaUserVO == null) {
                oaUserVO=userVO;
                // 经过网关转发过来的（能从网关获取到账号信息，网关已经鉴权）设置为跳过数据湖的权限校验
                passDatalakeAuth = true;
            }
//        DlApiQuery query = dlApiQueryService.findByQueryCode(apiCode);
//        if(!dlApiQueryService.hasPermission(account,query)){
//            throw new APIException(APIConstants.CODE_FAILD, "没有权限调用该API，没有权限");
//        }
            if (StringUtils.isEmpty(apiCode)) {
                throw new APIException(APIConstants.CODE_PARAM_ERR, "链接请求无效");
            }
            // region 处理请求参数 paramData
            String method = request.getMethod();
             paramData = new CaseInsensitiveMap<>();

            switch (method) {
                case "GET":
                    // get 默认用url参数
                    Enumeration names = request.getParameterNames();
                    while (names.hasMoreElements()) {
                        String name = (String) names.nextElement();
                        paramData.put(name.toLowerCase(), request.getParameter(name));
                    }
                    // 兼容json body
                    if (paramData.isEmpty()) {
                        if (jsonObject != null) {
                            paramData.putAll(jsonObject);
                        }
                    }
                    break;
                case "POST":
                    // post 默认用json body
                    if (jsonObject != null) {
                        paramData.putAll(jsonObject);
                    }
                    // 兼容 url参数
                    if (paramData.isEmpty()) {
                        Enumeration postParameterNames = request.getParameterNames();
                        while (postParameterNames.hasMoreElements()) {
                            String name = (String) postParameterNames.nextElement();
                            paramData.put(name.toLowerCase(), request.getParameter(name));
                        }
                    }
                    break;
                default:
                    throw new APIException(APIConstants.CODE_PARAM_ERR, "链接请求无效，仅支持POST、GET请求。");
            }
        /* 目前先注释掉 在查询接口的校验网关凭证的方法 ， 目前当以网关自身去判定是否需要校验凭证，和校验凭证有效性。
        //校验网关信息
        dlApiQueryService.verifyGateway(request.getHeader("Authorization"));
        */
            // endregion
            GetApiDataParamVo getApiDataParamVo = new GetApiDataParamVo(version, dlDataAccessLog, passDatalakeAuth);
            ApiTableDataPageVo apiTableDataPageVo = dlApiQueryService.getApiData(apiCode, paramData, oaUserVO, getApiDataParamVo);

            ResponseEntity<ApiTableDataPageVo> body = ResponseEntity.status(apiTableDataPageVo.getCode() == null ? HttpStatus.OK.value() : apiTableDataPageVo.getCode()).body(apiTableDataPageVo);
            //保存订阅数据使用日志
            dlDataAccessLog.setAccessState(Long.valueOf(HttpStatus.OK.value()));
            if (null != apiTableDataPageVo) {
                //获取返回参数
                List<ColumnInfoVo> columnInfoVoList = apiTableDataPageVo.getColumnInfoVoList();
                //获取返回数据量
                Long dataAmount = apiTableDataPageVo.getTotal();
                dlDataAccessLog.setResponseData(JSON.toJSONString(columnInfoVoList));
                dlDataAccessLog.setDataAmount(dataAmount);
            }

            //返回
            return body;
        } catch (APIException e) {
            e.printStackTrace();
            dlDataAccessLog.setErrorMsg(e.getMessage());
            dlDataAccessLog.setAccessState(Long.valueOf(e.getCode()));
            throw new APIException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            dlDataAccessLog.setErrorMsg(e.getMessage());
            dlDataAccessLog.setAccessState(Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            throw new APIException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        } finally {
//            try {
//                //保存订阅日志
//                dlDataAccessLog.setId(IdUtils.nextId());
//                dlDataAccessLog.setUserAccount(oaUserVO == null ? null : oaUserVO.getUserAccount());
//                dlDataAccessLog.setAccessType(ResourceConstant.ACCESS_TYPE_INTERFACE_QUERY);
//                //判断当前浏览器
//                BrowserUtils.Browser browser = BrowserUtils.getBrowser(request.getHeader(LoginUtils.USER_AGENT));
//                //如果不存在,则设置成其他
//                dlDataAccessLog.setUserAgent(browser == null ? BrowserUtils.Browser.OTHER.getDescribe() : browser.getDescribe());
//                dlDataAccessLog.setIpAddress(request.getRemoteAddr());
//                dlDataAccessLog.setDatetime(new Date());
//                dlDataAccessLogService.save(dlDataAccessLog);
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new APIException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "保存订阅日志异常");
//            }
        }
    }
}

