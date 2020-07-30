package com.openjava.datalake.rescata.service;

import com.openjava.datalake.common.*;
import com.openjava.datalake.component.AuditComponentService;
import com.openjava.datalake.rescata.domain.*;
import com.openjava.datalake.rescata.repository.DlRescataColumnRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author JiaHai
 * @Description 资源目录结构表业务层接口实现类
 */
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class DlRescataColumnServiceImpl implements DlRescataColumnService {

    @Resource
    private DlRescataColumnRepository dlRescataColumnRepository;
    @Resource
    private AuditComponentService auditComponentService;

    @Override
    public List<DlRescataColumn> findByResourceId(Long resourceId) {
        // 根据资源目录resourceId查询，并根据structureId顺序
        //List<DlRescataColumn> dlRescataColumnList = dlRescataColumnRepository.findByResourceIdOrderByStructureId(resourceId);
    	List<DlRescataColumn> dlRescataColumnList = dlRescataColumnRepository.findByResourceIdAndIsDeleteOrderByStructureId(resourceId, PublicConstant.NO);
        // 保存审计日志
        auditComponentService.saveAuditLogForQuery("资源目录", "资源目录列表", "数据项", "数据查询", resourceId + "");
        return dlRescataColumnList;
    }
}