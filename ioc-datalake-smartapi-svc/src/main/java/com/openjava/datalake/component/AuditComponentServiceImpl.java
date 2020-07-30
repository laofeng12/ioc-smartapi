package com.openjava.datalake.component;

import com.openjava.audit.auditManagement.component.AuditComponet;
import com.openjava.audit.auditManagement.vo.AuditLogVO;
import com.openjava.datalake.base.vo.DataOperateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author JiaHai
 * @Description 审计组件服务 业务层接口实现类
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AuditComponentServiceImpl implements AuditComponentService {
    @Autowired
    private AuditComponet auditComponet;

    @Override
    public void saveAuditLogForManagement(String operationService, String operationModule, String functionLevelOne, String functionLevelTwo, String recordId, DataOperateVo dataOperateVo,String... strs) {
        AuditLogVO auditLogVO = new AuditLogVO();
        // 管理操作
        auditLogVO.setType(1L);
        auditLogVO.setOperationService(operationService);
        auditLogVO.setOperationModule(operationModule);
        auditLogVO.setFunctionLev1(functionLevelOne);
        auditLogVO.setFunctionLev2(functionLevelTwo);
        auditLogVO.setRecordId(recordId);
        if (null != dataOperateVo) {
            auditLogVO.setDataBeforeOperat(dataOperateVo.getDataBeforeOperate());
            auditLogVO.setDataAfterOperat(dataOperateVo.getDataAfterOperate());
        }

        auditComponet.saveAuditLog(auditLogVO);
    }

    @Override
    public void saveAuditLogForQuery(String operationService, String operationModule, String functionLevelOne, String functionLevelTwo, String recordId,String... strs) {
        AuditLogVO auditLogVO = new AuditLogVO();
        // 数据查询
        auditLogVO.setType(2L);
        auditLogVO.setOperationService(operationService);
        auditLogVO.setOperationModule(operationModule);
        auditLogVO.setFunctionLev1(functionLevelOne);
        auditLogVO.setFunctionLev2(functionLevelTwo);
        auditLogVO.setRecordId(recordId);
        if (strs.length>=2){
            auditLogVO.setAccount(strs[0]);
            auditLogVO.setUserId(Long.valueOf(strs[1]));
        }
        auditComponet.saveAuditLog(auditLogVO);
    }

    @Override
    public void saveAuditLogForExport(String operationService, String operationModule, String functionLevelOne, String functionLevelTwo, Long fileId, String fileUrl,String... strs) {
        AuditLogVO auditLogVO = new AuditLogVO();
        //数据导出
        auditLogVO.setType(3L);
        auditLogVO.setOperationService(operationService);
        auditLogVO.setOperationModule(operationModule);
        auditLogVO.setFunctionLev1(functionLevelOne);
        auditLogVO.setFunctionLev2(functionLevelTwo);
        auditLogVO.setFileId(fileId);
        auditLogVO.setFileUrl(fileUrl);

        auditComponet.saveAuditLog(auditLogVO);
    }

    @Override
    public void saveAuditLogForImport(String operationService, String operationModule, String functionLevelOne, String functionLevelTwo, Long fileId, String fileUrl,String... strs) {
        AuditLogVO auditLogVO = new AuditLogVO();
        //数据导入
        auditLogVO.setType(4L);
        auditLogVO.setOperationService(operationService);
        auditLogVO.setOperationModule(operationModule);
        auditLogVO.setFunctionLev1(functionLevelOne);
        auditLogVO.setFunctionLev2(functionLevelTwo);
        auditLogVO.setFileId(fileId);
        auditLogVO.setFileUrl(fileUrl);

        auditComponet.saveAuditLog(auditLogVO);
    }
}
