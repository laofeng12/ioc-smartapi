package com.openjava.datalake.component;

import com.openjava.datalake.base.vo.DataOperateVo;

/**
 * @Author JiaHai
 * @Description 审计组件服务 业务层接口
 */
public interface AuditComponentService {
    /**
     * 记录管理操作日志
     *
     * @param operationService 操作的服务
     * @param operationModule  操作的模块
     * @param functionLevelOne 操作的功能点（一级）
     * @param functionLevelTwo 操作的功能点（二级）
     * @param recordId         操作的记录ID
     * @param dataOperateVo    操作前后的数据VO
     */
    void saveAuditLogForManagement(String operationService, String operationModule, String functionLevelOne, String functionLevelTwo, String recordId, DataOperateVo dataOperateVo, String... strs);

    /**
     * 记录查询操作日志
     *
     * @param operationService 操作的服务
     * @param operationModule  操作的模块
     * @param functionLevelOne 操作的功能点（一级）
     * @param functionLevelTwo 操作的功能点（二级）
     * @param recordId         查询的记录ID
     */
    void saveAuditLogForQuery(String operationService, String operationModule, String functionLevelOne, String functionLevelTwo, String recordId, String... strs);

    /**
     * 记录数据导出日志
     *
     * @param operationService 操作的服务
     * @param operationModule  操作的模块
     * @param functionLevelOne 操作的功能点（一级）
     * @param functionLevelTwo 操作的功能点（二级）
     * @param fileId           文件ID
     * @param fileUrl          文件URL
     */
    void saveAuditLogForExport(String operationService, String operationModule, String functionLevelOne, String functionLevelTwo, Long fileId, String fileUrl, String... strs);

    /**
     * 记录数据导入日志
     *
     * @param operationService 操作的服务
     * @param operationModule  操作的模块
     * @param functionLevelOne 操作的功能点（一级）
     * @param functionLevelTwo 操作的功能点（二级）
     * @param fileId           文件ID
     * @param fileUrl          文件URL
     */
    void saveAuditLogForImport(String operationService, String operationModule, String functionLevelOne, String functionLevelTwo, Long fileId, String fileUrl, String... strs);
}
