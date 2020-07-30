package com.openjava.datalake.push.service;

import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.common.responseBody.DtsDataSourceConnInfoResp;
import com.openjava.datalake.component.UserComponent;
import com.openjava.datalake.dataxjdbcutil.exception.DataXException;
import com.openjava.datalake.dataxjdbcutil.util.DbUtil;
import com.openjava.datalake.rescata.domain.DlRescataResource;
import com.openjava.datalake.rescata.domain.DlResourceDatasource;
import com.openjava.datalake.rescata.service.DlRescataDatabaseService;
import com.openjava.datalake.rescata.service.DlRescataResourceService;
import com.openjava.datalake.rescata.service.DlResourceDatasourceService;
import com.openjava.datalake.rescata.service.DtsService;
import com.openjava.datalake.rescata.vo.DatabaseInfoVo;
import com.openjava.datalake.util.JdbcUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.*;

/**
 * @Author xjd
 * @Date 2019/8/29 9:39
 * @Version 1.0
 */
@Service
@Transactional
public class ApiSyncServiceImpl implements ApiSyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSyncServiceImpl.class);

    @Resource
    DlRescataResourceService dlRescataResourceService;
    @Resource
    DlResourceDatasourceService dlResourceDatasourceService;
    @Resource
    DtsService dtsService;
    @Resource
    DlRescataDatabaseService dlRescataDatabaseService;
    @Value("${spring.profiles.active}")
    private String active;
    @Resource
    private UserComponent userComponent;
    /**
     * 获取数据库连接，同resourceId指定数据源
     * @param resourceId
     * @return
     * @throws APIException
     */
    @Override
    public Connection getConnThroughHikarByResId(Long resourceId) throws APIException {
        DlRescataResource dlRescataResource = dlRescataResourceService.findByResourceId(resourceId);
        Long databaseType;
        String driverClass;
        String username;
        String password;
        String jdbcUrl;
        if (ResourceConstant.RESOURCE_DATA_PROVIDE_MODE_PROCESSOR.equals(dlRescataResource.getSourceMode()) && PublicConstant.NO.equals(dlRescataResource.getIsInternalDataSource())) {
            // 库表挂载中的 外部数据源（汇聚平台）
            // 查询 资源目录关联数据源
            DlResourceDatasource dlResourceDatasource = dlResourceDatasourceService.findByResourceCode(dlRescataResource.getResourceCode());
            // TODO 对接获取数据源的jdbc
            DtsDataSourceConnInfoResp dtsDataSourceConnInfoResp = dtsService.queryDataByDataSourceConnInfo(dlResourceDatasource.getDatasourceId(), userComponent.getCurrentLoginUserInfo());
            driverClass = dtsDataSourceConnInfoResp.getJdbcDriverClass();
            jdbcUrl = dtsDataSourceConnInfoResp.getJDBCUrl(null);
            username = dtsDataSourceConnInfoResp.getUsername();
            password = dtsDataSourceConnInfoResp.getPassword();
        } else {
            databaseType = ResourceConstant.DATABASE_TYPE_MPP;
            driverClass = JdbcUtils.getDriverByDatabaseType(databaseType);
            DatabaseInfoVo databaseInfoVo = dlRescataDatabaseService.findByResourceCodeAndRepositoryType(dlRescataResource.getResourceCode(), databaseType);
//            if (active.contains("dev")) {
//                databaseInfoVo.setIp("nhc.smart-info.cn");
//                databaseInfoVo.setPort("54321");
//            }
            username = databaseInfoVo.getUsername();
            password = databaseInfoVo.getPassword();
            jdbcUrl = JdbcUtils.generateJdbcUrl(databaseType, databaseInfoVo.getIp(), databaseInfoVo.getPort(), databaseInfoVo.getDatabaseName(), databaseInfoVo.getSchema());
        }

        Connection connection = null;
        try {
            connection = DbUtil.getConnByHikar(jdbcUrl, username, password, driverClass);
        } catch (DataXException e) {
            throw new APIException(APIConstants.CODE_SERVER_ERR, e.getMessage());
        }
        return connection;
    }
}
