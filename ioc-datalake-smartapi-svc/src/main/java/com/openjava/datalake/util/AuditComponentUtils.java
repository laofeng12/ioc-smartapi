package com.openjava.datalake.util;

import com.alibaba.fastjson.JSON;
import com.openjava.audit.auditManagement.component.AuditComponet;
import com.openjava.audit.auditManagement.vo.AuditLogVO;
import org.ljdp.common.spring.SpringContextManager;

/**
 * 审计日志保存工具
 *
 * @author lwx
 * @date 19/11/24
 */
public class AuditComponentUtils {

    private static AuditComponet auditComponet = SpringContextManager.getBean(AuditComponet.class);

    public static void genAudit(Long type, String operationService, String operationModule, String recordId, Object dataBefore, Object dataAfter, String... function) {
        AuditLogVO vo = new AuditLogVO();
        vo.setType(type);
        vo.setOperationService(operationService);
        vo.setOperationModule(operationModule);
        vo.setRecordId(recordId);
        vo.setDataBeforeOperat(dataBefore == null ? null : JSON.toJSONString(dataBefore));
        vo.setDataAfterOperat(dataAfter == null ? null : JSON.toJSONString(dataAfter));
        if (function.length >= 1) {
            vo.setFunctionLev1(function[0]);
            if (function.length == 2) {
                vo.setFunctionLev2(function[1]);
            }
        }
        auditComponet.saveAuditLog(vo);
//        try {
//            System.out.println("保存审计日志成功！");
//        } catch (Exception e) {
//            System.out.println("保存审计日志失败:" + vo.getOperationService() + "-" + vo.getOperationModule());
//        }
    }
}
