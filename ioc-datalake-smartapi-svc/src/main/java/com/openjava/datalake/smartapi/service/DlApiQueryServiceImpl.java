package com.openjava.datalake.smartapi.service;

import com.alibaba.fastjson.JSON;
import com.openjava.admin.user.vo.OaOrgVO;
import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.common.responseBody.DtsDataSourceConnInfoResp;
import com.openjava.datalake.rescata.domain.*;
import com.openjava.datalake.rescata.repository.DlRescataResourcePermissionRepository;
import com.openjava.datalake.rescata.repository.DlRescataStructurePermissionRepository;
import com.openjava.datalake.rescata.service.*;
import com.openjava.datalake.rescata.vo.ColumnInfoVo;
import com.openjava.datalake.rescata.vo.DataWithColumn;
import com.openjava.datalake.rescata.vo.DatabaseInfoVo;
import com.openjava.datalake.rescata.vo.TableData;
import com.openjava.datalake.smartapi.domain.DlApiQuery;
import com.openjava.datalake.smartapi.domain.DlApiRequest;
import com.openjava.datalake.smartapi.domain.DlApiResponse;
import com.openjava.datalake.smartapi.repository.DlApiQueryRepository;
import com.openjava.datalake.smartapi.repository.DlApiRequestRepository;
import com.openjava.datalake.smartapi.repository.DlApiResponseRepository;
import com.openjava.datalake.smartapi.vo.*;
import com.openjava.datalake.subscribe.domain.DlDataAccessLog;
import com.openjava.datalake.subscribe.domain.vo.PermiFilterResourceInfoParamVo;
import com.openjava.datalake.subscribe.service.DlSubscribeCatalogFormService;
import com.openjava.datalake.util.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DL_API_QUERY业务层
 *
 * @author zjf
 */
@Service
@Log4j2
@Transactional
public class DlApiQueryServiceImpl implements DlApiQueryService {

    @Autowired
    DlRescataResourcePermissionRepository dlRescataResourcePermissionRepository;
    @Autowired
    private DlSubscribeCatalogFormService dlSubscribeCatalogFormService;
    @Resource
    private DlRescataStructurePermissionService dlRescataStructurePermissionService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private DlApiQueryRepository dlApiQueryRepository;
    @Resource
    private DlApiRequestRepository dlApiRequestRepository;
    @Resource
    private DlApiResponseRepository dlApiResponseRepository;
    @Autowired
    private DlRescataResourceService dlRescataResourceService;
    @Resource
    private DlResourceDatasourceService dlResourceDatasourceService;
    @Resource
    private DlRescataResourcePermissionService dlRescataResourcePermissionService;
    @Resource
    private DtsService dtsService;
    @Autowired
    private DlRescataDatabaseService dlRescataDatabaseService;
    @Autowired
    private DlRescataColumnService dlRescataColumnService;
    @Resource
    private DlRescataStructurePermissionRepository dlRescataStructurePermissionRepository;
    @Override
    public ApiTableDataPageVo getApiData(String apiCode, Map<String, Object> paramData, OaUserVO oaUserVO, GetApiDataParamVo getApiDataParamVo)
            throws APIException {
        //先加载api配置信息
        QueryApiInfoVO queryApiInfoVO = getQueryApiInfoVO(apiCode, oaUserVO);
        DlApiQuery apiQuery = queryApiInfoVO.getApiQuery();
        Boolean passDatalakeAuth = getApiDataParamVo.getPassDatalakeAuth();
        if(!this.hasPermission(oaUserVO.getUserAccount(), queryApiInfoVO) && !passDatalakeAuth){
            throw new APIException(APIConstants.CODE_FAILD, "没有权限调用该API，请先订阅");
        }
        List<DlApiRequest> apiRequests = queryApiInfoVO.getApiRequests();
        List<DlApiResponse> apiResponse = queryApiInfoVO.getApiResponse();
        ResourceTableVO resourceTableVO = queryApiInfoVO.getResourceTableVO();

        DataQueryParam dataQueryParam = new DataQueryParam();
        dataQueryParam.setResourceId(apiQuery.getResourceId());
        dataQueryParam.setResourceCode(apiQuery.getResourceCode());
        dataQueryParam.setSearch("");
        //保存订阅数据使用日志的ResourceCode和请求参数
        @NotNull DlDataAccessLog dlDataAccessLog = getApiDataParamVo.getDlDataAccessLog();
        dlDataAccessLog.setRequestParam(JSON.toJSONString(apiRequests));
        dlDataAccessLog.setResourceCode(apiQuery.getResourceCode());

        if (paramData.get("getTotal")!=null){
            dataQueryParam.setGetTotal(Boolean.valueOf((String) paramData.get("getTotal")));
        }
        //region 若不分页，则默认查询前500条数据 dataQueryParam.setSize setPage
        int pageNum = 0, pageSize = 20;

        if (apiQuery.getPageMark() == 1) {
            //若分页，但是没有传入分页参数时，则获取设置的分页数量
            pageNum = StringUtils.isEmpty(paramData.get("pagenum")) ? 0 : Integer.parseInt(paramData.get("pagenum").toString());
            pageSize = StringUtils.isEmpty(paramData.get("pagesize")) ? Integer.parseInt(apiQuery.getPageSize().toString()) : Integer.parseInt(paramData.get("pagesize").toString());
        }
        dataQueryParam.setPage(pageNum);
        dataQueryParam.setSize(pageSize);

        // region 校验请求参数 并拼装参数 dataQueryParam.setColumnParamList

        dataQueryParam.setColumnParamList(new ArrayList<>());
        for (DlApiRequest apiRequest : apiRequests) {
            String columnId = apiRequest.getColumnId();
            String columnDefinition = apiRequest.getColumnDefinition();//绑定字段
            String defaultValue = apiRequest.getDefaultValue();//默认值
            Boolean requiredMark = apiRequest.getRequiredMark() == 1;//必填
            Boolean fuzzyMark = apiRequest.getFuzzyMark() == 1;//模糊搜索

            ColumnParam columnParam = new ColumnParam();
            columnParam.setColumnId(Long.parseLong(columnId));
            columnParam.setColumnCode(apiRequest.getColumnCode());
            columnParam.setColumnDefinition(columnDefinition);
            columnParam.setFuzzySearch(fuzzyMark);

            //当用户的请求参数中是否包含该字段
            boolean containsKey = paramData.containsKey(columnDefinition.toLowerCase());
            if (containsKey) {
                //当用户的请求参数中传入了该字段时，读取该字段对应的值，包括空值。
                String paramValue = (String) paramData.get(columnDefinition.toLowerCase());
                //参数值是否为空：是否必填 && 传入的参数值是否为空 && 参数默认值是否为空
                boolean isRequireNoValue = requiredMark && StringUtils.isEmpty(paramValue)
                        && StringUtils.isEmpty(defaultValue);
                //#263-bug : 当必填参数的参数值不填时，不做筛选返回了所有数据
                if (isRequireNoValue) {
                    throw new APIException(APIConstants.CODE_PARAM_ERR, "获取数据失败，必填参数缺少参数值");
                }
                // 设置参数值
                columnParam.setColumnValue((paramValue == null || ("").equals(paramValue)) ? defaultValue : paramValue);
            } else {
                //当用户的请求参数中不包含必须传入的参数时，抛出错误信息。若不是必须传入的参数，则设置该字段的值为默认值
                if (requiredMark) {
                    throw new APIException(APIConstants.CODE_PARAM_ERR, "获取数据失败，缺少必须传入的参数");
                } else {
                    columnParam.setColumnValue(defaultValue);
                }
            }
            //参数没有值时不拼接进请求参数中
            if (!StringUtils.isEmpty(columnParam.getColumnValue())) {
                dataQueryParam.addColumnParam(columnParam);
            }
        }
        //添加自定义sql 紧急处理二标四实需求
        String whereAppend =" ";
        if (CollectionUtils.isEmpty(dataQueryParam.getColumnParamList())){
            whereAppend = " where ";
        }else {
            whereAppend = " and ";
        }
        //添加自定义where条件
        if  (org.apache.commons.lang3.StringUtils.isNotBlank(apiQuery.getSelfConditionSql())){
            dataQueryParam.setSelfConditionSql(whereAppend +" "+ apiQuery.getSelfConditionSql()+" ");
        }
        //#region 拼接 需要查询的字段 dataQueryParam.setSelectColumnIdList

        dataQueryParam.setSelectColumnList(apiResponse);

        List<Long> selectColumnIds = apiResponse.stream().map(m -> Long.parseLong(m.getColumnId())).collect(Collectors.toList());
        dataQueryParam.setSelectColumnIdList(selectColumnIds);
        //#endregion

        return this.queryDataPageByResourceId(dataQueryParam, resourceTableVO, oaUserVO, passDatalakeAuth);

        // return null;
    }
    //#region 获取数据
    private ApiTableDataPageVo queryDataPageByResourceId(DataQueryParam dataQueryParam, ResourceTableVO resourceTableVO, OaUserVO oaUserVO, boolean passDatalakeAuth) throws APIException {
        String userAccount = oaUserVO.getUserAccount();
        // 检查参数
        DataQueryParam.checkRequire(dataQueryParam);
        //OaUserVO oaUserVO = (OaUserVO) SsoContext.getUser();

        //提示消息
        List<ApiWarningMessageVo> warningMessage = new ArrayList<>();

        Long resourceId = dataQueryParam.getResourceId();
        int page = dataQueryParam.getPage();
        int size = dataQueryParam.getSize();
        // 用户期待展示的字段
        List<Long> selectColumnIdList = dataQueryParam.getSelectColumnIdList();
        List<DlApiResponse> selectColumnList = dataQueryParam.getSelectColumnList();
        List<ColumnParam> columnParamList = dataQueryParam.getColumnParamList();
        String search = dataQueryParam.getSearch();

        // 用于组装SQL的select的字段
        List<DlRescataColumn> selectDlRescataColumnList = new ArrayList<>();

        // 1、查询资源目录
        DlRescataResource dlRescataResource = resourceTableVO.getDlRescataResource();
//        // 2、设置 “数据库类型” （若分库类别为“归集库”，则设为HIVE；否则设为MPP）
//        Long databaseType = this.setDatabaseTypeByRepositoryType(dlRescataResource.getRepositoryType());
        // 3、获取数据库连接信息
        DatabaseInfoVo databaseInfoVo = resourceTableVO.getDatabaseInfoVo();
//        if ("dev".equals(active) || "dmpv3dev".equals(active)) {
//            databaseInfoVo.setIp("nhc.smart-info.cn").setPort("54321");
//        }
        // 4、查询该资源目录“全部”的字段
        List<DlRescataColumn> dlRescataColumnList = resourceTableVO.getDlRescataColumnList();
        List<Long> isListColumnIdList = dlRescataColumnList.stream().map(DlRescataColumn::getStructureId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(selectColumnIdList)) {
            // 用户已选择期待展示的字段
            // 校验 用户期待展示的字段（不可多于“可用于展示”的字段、只能属于“可用于展示”的字段中）
//            if (selectColumnIdList.size() > isListColumnIdList.size()) {
//                throw new APIException(APIConstants.CODE_PARAM_ERR, "获取数据失败，返回参数 不能多于 该查询接口的资源目录的信息项");//所选信息项 不能多于 该资源目录可用于展示的信息项
//            } else {
//                for (Long selectColumnId : selectColumnIdList) {
//                    if (!isListColumnIdList.contains(selectColumnId)) {
//                        throw new APIException(APIConstants.CODE_PARAM_ERR,"所选返回参数中有 不存在 该查询接口的资源目录的信息项");
//                    }
//                }
            for (DlApiResponse response : selectColumnList) {
                boolean isMatch = false;
                for (DlRescataColumn rescataColumn : dlRescataColumnList) {
                    if (rescataColumn.getColumnCode().equals(response.getColumnCode())) {
                        selectDlRescataColumnList.add(rescataColumn);
                        isMatch = true;
                        break;
                    }
                }
                if (!isMatch) {
                    warningMessage.add(new ApiWarningMessageVo(response.getColumnName(), "返回参数失效"));
                }
            }
//                for (Long selectColumnId : selectColumnIdList) {
//                    boolean isMatch = false;
//                    for (DlRescataColumn rescataColumn : isListDlRescataColumnList) {
//                        if (rescataColumn.getStructureId().equals(selectColumnId)) {
//                            selectDlRescataColumnList.add(rescataColumn);
//                            isMatch = true;
//                            break;
//                        }
//                    }
//                    if (!isMatch) {
//                        throw new APIException(APIConstants.CODE_PARAM_ERR, "获取数据失败，所选返回参数中有 不存在 该查询接口的资源目录的信息项");
//                    }
//                }
//            }


//            selectDlRescataColumnList = dlRescataColumnService.findByStructureIds(selectColumnIdList);
        }
        //else {
        // 用户未选择期待展示的字段（根据该资源目录“可用于查询”的信息项，查询数据）
        //  selectDlRescataColumnList = isListDlRescataColumnList;
        // }

        // 5、查询该资源目录“可用于展示”的字段
        List<DlRescataColumn> isQueryDlRescataColumnList = dlRescataColumnList;
        // 6、查询该资源目录“可用于展示”、且当前用户有权限的字段
        /*
        List<DlRescataColumn> isQueryPermittedDlRescataColumnList = dlSubscribeColumnService.getPermittedColumn(isQueryDlRescataColumnList, oaUserVO.getUserAccount());
        if (CollectionUtils.isEmpty(isQueryPermittedDlRescataColumnList) && CollectionUtils.isNotEmpty(columnParamList)) {
            throw new APIException("当前用户没有可用的查询条件");
        }
        */
        List<Long> isQueryPermitterColumnIdList = isListColumnIdList;//isQueryPermittedDlRescataColumnList.stream().map(DlRescataColumn::getStructureId).collect(Collectors.toList());
        // 校验 查询条件（不可多于“可用于查询”的字段、只能属于“可用于查询”的字段中）
//        if (columnParamList.size() > isQueryPermitterColumnIdList.size()) {
//            throw new APIException(APIConstants.CODE_PARAM_ERR, "获取数据失败，所选查询条件 不能多于 该查询接口的信息项");
//        } else {
        for (ColumnParam columnParam : columnParamList) {
//                for (DlRescataColumn column : isQueryDlRescataColumnList){
            boolean isMatch = false;
            for (DlRescataColumn rescataColumn : dlRescataColumnList) {
                if (rescataColumn.getColumnCode().equals(columnParam.getColumnCode())) {
                    columnParam.setColumnDefinition(rescataColumn.getColumnDefinition());
                    columnParam.setDataType(rescataColumn.getDataType());
                    isMatch = true;
                    break;
                }
            }
            if (!isMatch) {
                warningMessage.add(new ApiWarningMessageVo(columnParam.getColumnDefinition(), "请求参数失效"));
            }
//                }
//                if (!isQueryPermitterColumnIdList.contains(columnParam.getColumnId())) {
//                    throw new APIException(APIConstants.CODE_PARAM_ERR, "获取数据失败，所选查询条件中有 不存在 该查询接口的资源目录的信息项");
//                }
//                // 补全信息（用于生成SQL的 WHERE条件）
//                isQueryDlRescataColumnList.stream().forEach(dlRescataColumn -> {
//                    if (dlRescataColumn.getStructureId().equals(columnParam.getColumnId())) {
//                        columnParam.setColumnDefinition(dlRescataColumn.getColumnDefinition());
//                        columnParam.setDataType(dlRescataColumn.getDataType());
//                    }
//                });
        }
//        }
        //  System.out.println("参数List：" + JSON.toJSON(columnParamList));

        // 判断数据库中是否存在该表
        //this.isExistInDatabase(databaseInfoVo, databaseType, (dlRescataResource.getResourceTableName()));
        if(warningMessage.size() > 0){
            System.out.println("warning message:" + warningMessage);
        }
        if (CollectionUtils.isEmpty(selectDlRescataColumnList)) {
            ApiTableDataPageVo apiTableDataPageVo = new ApiTableDataPageVo();
            apiTableDataPageVo.setCode(APIConstants.CODE_FAILD);
            apiTableDataPageVo.setMessageList(warningMessage);
            apiTableDataPageVo.setMessage("返回参数全部失效");
            return apiTableDataPageVo;
        }
        StringBuffer resourceTableName = new StringBuffer(dlRescataResource.getResourceTableName());
        Long isInternalDataSource = dlRescataResource.getIsInternalDataSource();
        if (PublicConstant.NO.equals(isInternalDataSource)) {
            JdbcApiQueryUtils.amemdRealSelectAndWhereColumnList(selectDlRescataColumnList, columnParamList, resourceTableName, databaseInfoVo);
        }
        // 生成统计SQL，得到总数量
        String countSql = JdbcApiQueryUtils.generateCountSql(resourceTableName.toString(), selectDlRescataColumnList, databaseInfoVo.getDatabaseType(), columnParamList ,dataQueryParam.getSelfConditionSql());
        Long amount = 0L;
        System.out.println("查询总数"+dataQueryParam.isGetTotal());
        if (dataQueryParam.isGetTotal()){
            amount = JdbcApiQueryUtils.executeCountBeta(columnParamList,databaseInfoVo, databaseInfoVo.getDatabaseType(), countSql);
        }
//        System.out.println("amount:"+amount);
        // 生成查询SQL （得到 tableHeader（英文） 和 tableBody）
        String sql = JdbcApiQueryUtils.generateSql(resourceTableName.toString(), selectDlRescataColumnList, search, columnParamList, databaseInfoVo.getDatabaseType(), page, size,dataQueryParam.getSelfConditionSql());

        DataWithColumn dataWithColumn = JdbcApiQueryUtils.executeQueryBeta(selectDlRescataColumnList, search, columnParamList,databaseInfoVo, databaseInfoVo.getDatabaseType(), sql);

        // 组装返回结果 TableData
        TableData tableData = new TableData(null, dataWithColumn.getData(), selectDlRescataColumnList);
        // 权限过滤（数据加密、脱敏）
        TableData permissionTableData;

        if (    // 挂载外部数据源的不用数据湖权限校验
                (ResourceConstant.RESOURCE_DATA_PROVIDE_MODE_PROCESSOR.equals(dlRescataResource.getSourceMode())
                        && PublicConstant.NO.equals(dlRescataResource.getIsInternalDataSource()))
                        // 从网关转发过来的不用走数据湖的鉴权
                        || passDatalakeAuth) {
            permissionTableData = tableData;
            permissionTableData.setPermissionLevel(PublicConstant.RESOURCE_PERMISSION_LEVEL_ALL);
        } else {
            List<DlRescataStrucPermi> structurePermissions = resourceTableVO.getStructurePermissions();
            PermiFilterResourceInfoParamVo permiFilterResourceInfoParamVo = new PermiFilterResourceInfoParamVo(dlRescataColumnList, dlRescataResource, structurePermissions);
            permissionTableData = dlSubscribeCatalogFormService.permissionFilter2(tableData, userAccount, permiFilterResourceInfoParamVo);
        }

        // 简化信息项List，得到tableData的header， 并且在表头标记字段权限
        List<ColumnInfoVo> columnInfoVoList = this.simplifyDlrescataColumn(selectDlRescataColumnList, permissionTableData.getStructurePermissions(), userAccount);
        ApiTableDataPageVo apiTableDataPageVo = new ApiTableDataPageVo();
        MySpringBeanUtils.copyPropertiesNotBlank(permissionTableData, apiTableDataPageVo);
        apiTableDataPageVo.setColumnInfoVoList(columnInfoVoList);
//        apiTableDataPageVo.setData(permissionTableData.getData());
        apiTableDataPageVo.setMessageList(warningMessage);
        apiTableDataPageVo.setPage(page);
        apiTableDataPageVo.setSize(size);
        apiTableDataPageVo.setTotal(amount);
//        return new ApiTableDataPageVo(columnInfoVoList, permissionTableData.getData(), permissionTableData.getPermissionLevel(), page, size, amount, warningMessage);
        ApiTableDataPageVo apiTableDataPageVo1 = MyJsonUtils.processJsonAccuracy(apiTableDataPageVo);
        return apiTableDataPageVo1;
    }
    /**
     * 简化信息项List（用于返回的tableData的header）
     *
     * @param selectDlrescataColumnList
     * @return
     */
    private List<ColumnInfoVo> simplifyDlrescataColumn(List<DlRescataColumn> selectDlrescataColumnList, List<DlRescataStrucPermi> structurePermissions, String userAccount) {
        List<ColumnInfoVo> columnInfoVoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(selectDlrescataColumnList)) {
            Map<Long, DlRescataStrucPermi> structurePermissionMap;
            if (structurePermissions != null) {//先判断是否已经查过了，不要重复连数据库
                structurePermissionMap = structurePermissions.stream().collect(Collectors.toMap(DlRescataStrucPermi::getStructureId, Function.identity()));
            } else {
                structurePermissionMap = dlRescataStructurePermissionService.findAlreadyExistPermittedColumn(selectDlrescataColumnList, userAccount);
            }


            for (DlRescataColumn dlRescataColumn : selectDlrescataColumnList) {

                ColumnInfoVo columnInfoVo = new ColumnInfoVo();
                columnInfoVo.setColumnId(dlRescataColumn.getStructureId());
                columnInfoVo.setColumnDefinition(dlRescataColumn.getColumnDefinition());
                columnInfoVo.setColumnName(dlRescataColumn.getColumnName());
                columnInfoVo.setResourceId(dlRescataColumn.getResourceId());

                // 设置字段权限
                DlRescataStrucPermi dlRescataStrucPermi = structurePermissionMap.get(dlRescataColumn.getStructureId());
                columnInfoVo.setViewable(false);
                columnInfoVo.setDecryption(false);
                columnInfoVo.setSensitived(false);
                if (dlRescataStrucPermi != null) {
                    columnInfoVo.setViewable(true);
                    Long isDesensitization = dlRescataColumn.getIsDesensitization();
                    Long isSensitived = dlRescataStrucPermi.getIsSensitived();
                    // 不需要脱敏的字段，或者有脱敏权限的
                    if (!PublicConstant.YES.equals(isDesensitization) || PublicConstant.YES.equals(isSensitived)) {
                        columnInfoVo.setSensitived(true);
                    }
                    Long isEncrypt = dlRescataColumn.getIsEncrypt();
                    Long isDecryption = dlRescataStrucPermi.getIsDecryption();
                    // 不需要加密的字段，或者拥有不加密的权限
                    if (!PublicConstant.YES.equals(isEncrypt) || PublicConstant.YES.equals(isDecryption)) {
                        columnInfoVo.setDecryption(true);
                    }
                }

                columnInfoVoList.add(columnInfoVo);
            }
        }
        return columnInfoVoList;
    }


    //从缓存加载API信息
    private QueryApiInfoVO getQueryApiInfoVO(String apiCode, OaUserVO oaUserVO) throws APIException {
        final String cacheKey = QueryApiInfoVO.KEY_API_CODE + apiCode;
        QueryApiInfoVO queryApiInfoVO = (QueryApiInfoVO)redisTemplate.opsForValue().get(cacheKey);
        if(queryApiInfoVO == null){
            //获取接口配置
            DlApiQuery apiQuery = dlApiQueryRepository.findByQueryCodeAndDeleteMarkAndValidMark(apiCode, 0L, 1L);
            if (apiQuery == null) {
                throw new APIException(APIConstants.CODE_PARAM_ERR, "链接请求无效");
            }
            List<DlApiRequest> apiRequests = dlApiRequestRepository.findByQueryId(apiQuery.getId());
            // 零星需求-直接连URL必须带查询条件
            if (apiRequests.isEmpty()) {
//                throw new APIException(APIConstants.CODE_PARAM_ERR, "获取数据失败，查询条件不可为空");
            }
            List<DlApiResponse> apiResponse = dlApiResponseRepository.findByQueryId(apiQuery.getId());
            if (apiResponse.size() <= 0) {
                throw new APIException(APIConstants.CODE_PARAM_ERR, "获取数据失败，没有需要查询的字段。");
            }
            // 1、查询资源目录
            DlRescataResource dlRescataResource = dlRescataResourceService.getByResourceId(apiQuery.getResourceId());
            if (null == dlRescataResource) {
                throw new APIException(APIConstants.CODE_PARAM_ERR, "获取数据失败，该查询接口的资源目录不存在");
            }
            if (!ResourceConstant.RESOURCE_STATE_EFFECTIVE.equals(dlRescataResource.getResourceState())) {
                throw new APIException(APIConstants.CODE_PARAM_ERR, "获取数据失败，该查询接口的资源目录已被停用");
            }
            //2、获取数据库类型
            Long databaseType = this.setDatabaseTypeByRepositoryType(dlRescataResource.getRepositoryType());
            // 3、获取数据库连接信息
            DatabaseInfoVo databaseInfoVo = null;
            if (ResourceConstant.RESOURCE_DATA_PROVIDE_MODE_PROCESSOR.equals(dlRescataResource.getSourceMode()) && PublicConstant.NO.equals(dlRescataResource.getIsInternalDataSource())) {
                // 库表挂载中的 外部数据源（汇聚平台）
                // 查询 资源目录关联数据源
                DlResourceDatasource dlResourceDatasource = dlResourceDatasourceService.findByResourceCode(dlRescataResource.getResourceCode());

                Long resourcePermissionLevel = dlRescataResourcePermissionService.getResourcePermissionLevel(dlRescataResource.getResourceId(), oaUserVO.getUserAccount());
                OaUserVO createResUser = null;
                if (!PublicConstant.RESOURCE_PERMISSION_LEVEL_NONE.equals(resourcePermissionLevel)) {
                    // 提取资源目录的创建人信息到OaUserVo
                    createResUser = extractCreateUserVo(dlRescataResource);
                }

                // 对接获取数据源的jdbc
                DtsDataSourceConnInfoResp dtsDataSourceConnInfoResp = dtsService.queryDataByDataSourceConnInfo(dlResourceDatasource.getDatasourceId(), createResUser != null ? createResUser : oaUserVO);
//                String jdbcDriverClass = dtsDataSourceConnInfoResp.getJdbcDriverClass();
//                String jdbcUrl = dtsDataSourceConnInfoResp.getJDBCUrl(null);
                databaseInfoVo = dtsDataSourceConnInfoResp.converToDatabaseInfoVo(null);
            } else {
                databaseInfoVo = dlRescataDatabaseService.findByResourceCodeAndRepositoryType(dlRescataResource.getResourceCode(), databaseType);
//                if (active.contains("dev")) {
//                    databaseInfoVo.setIp("nhc.smart-info.cn");
//                    databaseInfoVo.setPort("54321");
//                }
            }
            // 4、查询该资源目录“全部”的字段
            List<DlRescataColumn> dlRescataColumnList = dlRescataColumnService.findByResourceId(dlRescataResource.getResourceId());
            if (CollectionUtils.isEmpty(dlRescataColumnList)) {
                throw new APIException(APIConstants.CODE_PARAM_ERR, "获取数据失败，该查询接口的资源目录没有信息项");
            }
            //5、获取已经申请通过的权限
            List<Long> structureIds = dlRescataColumnList.stream().map(DlRescataColumn::getStructureId).collect(Collectors.toList());
            List<DlRescataStrucPermi> structurePermissions = dlRescataStructurePermissionRepository.findByStructureIdInAndOwnerAccount(structureIds, oaUserVO.getUserAccount());

            ResourceTableVO resourceTableVO = new ResourceTableVO(dlRescataResource, databaseInfoVo, dlRescataColumnList, structurePermissions);

            queryApiInfoVO = new QueryApiInfoVO();
            queryApiInfoVO.setApiQuery(apiQuery);
            queryApiInfoVO.setApiRequests(apiRequests);
            queryApiInfoVO.setApiResponse(apiResponse);
            queryApiInfoVO.setResourceTableVO(resourceTableVO);

            redisTemplate.opsForValue().set(cacheKey, queryApiInfoVO, Duration.ofMinutes(5));
        }
        return queryApiInfoVO;
    }

    /**
     * 提取资源目录的创建人信息到OaUserVo
     * @param dlRescataResource
     * @return
     */
    private OaUserVO extractCreateUserVo(DlRescataResource dlRescataResource) {
        OaUserVO createResUser;
        String createFullname = dlRescataResource.getCreateFullname();
        String createAccount = dlRescataResource.getCreateAccount();
        Long createUuid = dlRescataResource.getCreateUuid();
        String createDeptCode = dlRescataResource.getCreateDeptCode();
        String createDeptId = dlRescataResource.getCreateDeptId();
        String createDeptName = dlRescataResource.getCreateDeptName();
        String createDeptTopId = dlRescataResource.getCreateDeptTopId();
        String createDeptTopCode = dlRescataResource.getCreateDeptTopCode();
        String createDeptTopName = dlRescataResource.getCreateDeptTopName();

        createResUser = new OaUserVO();
        createResUser.setUserName(createFullname);
        createResUser.setUserId(String.valueOf(createUuid));
        createResUser.setUserAccount(createAccount);
        createResUser.setOrgid(createDeptId);
        createResUser.setOrgcode(createDeptCode);
        createResUser.setOrgname(createDeptName);
        OaOrgVO topOrg = createResUser.getTopOrg();
        if (topOrg != null) {
            topOrg.setOrgname(createDeptTopName);
            topOrg.setOrgid(createDeptTopId);
            topOrg.setOrgcode(createDeptTopCode);
        }
        return createResUser;
    }

    public boolean hasPermission(String userAccount, QueryApiInfoVO queryApiInfoVO) throws APIException {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userAccount)) {
            DlApiQuery apiQuery = queryApiInfoVO.getApiQuery();
            ResourceTableVO resourceTableVO = queryApiInfoVO.getResourceTableVO();
            DlRescataResource dlRescataResource = resourceTableVO.getDlRescataResource();
            //是否创建者
            if (userAccount.equals(apiQuery.getCreateUserAccount())) {
                return true;
            }
            if (userAccount.equals(dlRescataResource.getBusinessOwnerAccount())) {
                return true;
            }
            //是否订阅
            DlRescataResourcePermission rp = dlRescataResourcePermissionRepository.findByResourceIdAndOwnerAccount(dlRescataResource.getResourceId(), userAccount);
            if (rp!=null&&rp.getPermissionLevel()<=3) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置 “数据库类型” （若分库类别为“归集库”，则设为HIVE；否则设为MPP）
     *
     * @param repositoryType
     * @return
     */
    private Long setDatabaseTypeByRepositoryType(Long repositoryType) {
        return ResourceConstant.DATABASE_TYPE_MPP;
    }
}
