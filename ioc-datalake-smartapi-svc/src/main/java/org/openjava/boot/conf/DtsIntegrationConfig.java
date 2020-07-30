package org.openjava.boot.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiahai
 */
@Configuration
@ConfigurationProperties(prefix = "dts.integration.datasource")
@Getter
@Setter
public class DtsIntegrationConfig {
    /**
     * URL 根据数据库类型， 查询数据库
     */
    private String search;

    /**
     * URL 根据数据库， 查询表
     */
    private String getTableList;

    /**
     * URL 根据表， 查询字段
     */
    private String getColumnList;

    /**
     * URL 统计表的数据量（select count(1) from table）
     */
    private String countByTable;

    /**
     * URL 备库数据资产统计结果
     */
    private String resultSearch;

    /**
     * URL 根据资源目录编码获取任务信息
     */
    private String getTaskInfo;

    /**
     * URL 根据数据源ID和表名，分页查询表数据
     */
    private String queryTableData;
    /**
     * URL 通过数据源ID获取加密后的JDBC链接信息
     */
    private String getDataSourceConnInfo;
    /**
     * URL 根据表名获取时序值本源值(数字)
     */
    private String queryDoSeqNumber;
    /**
     * URL 根据表名更新时序数
     */
    private String updateDoSeqNumber;
}
