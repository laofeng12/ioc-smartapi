package com.openjava.datalake.rescata.service;

import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.component.AuditComponentService;
import com.openjava.datalake.rescata.domain.DlRescataResource;
import com.openjava.datalake.rescata.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author JiaHai
 * @Description 资源目录宽表 业务层接口实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DlRescataResourceServiceImpl implements DlRescataResourceService {
    @Autowired
    private DlRescataResourceRepository dlRescataResourceRepository;


    @Autowired
    private AuditComponentService auditComponentService;

    @Override
    public DlRescataResource findByResourceId(Long resourceId) {
        DlRescataResource dlRescataResource = dlRescataResourceRepository.findByResourceId(resourceId);
        // 保存审计日志
        auditComponentService.saveAuditLogForQuery("数据湖", "资源目录", "资源目录列表", "基本信息", resourceId + "");
        return dlRescataResource;
    }

    @Override
    public DlRescataResource getByResourceId(Long resourceId) throws APIException {
        Optional<DlRescataResource> optionalDlRescataResource = dlRescataResourceRepository.findById(resourceId);
        boolean present = optionalDlRescataResource.isPresent();
        if (present) {
            DlRescataResource dlRescataResource = optionalDlRescataResource.get();
            // 保存审计日志
            auditComponentService.saveAuditLogForQuery("数据湖", "资源目录", "资源目录列表", "基本信息", resourceId + "");
            return dlRescataResource;
        } else {
            throw new APIException(APIConstants.CODE_PARAM_ERR, "资源目录ID有误");
        }
    }

    @Override
    public DlRescataResource queryLatestByResourceId(Long resourceId) throws APIException {
        DlRescataResource rescataResource = dlRescataResourceRepository.findByResourceIdAndIsLatest(resourceId, PublicConstant.YES);
        if (rescataResource == null) {
            throw new APIException(APIConstants.CODE_FAILD, "资源目录ID无效");
        }
        return rescataResource;
    }

    @Override
    public DlRescataResource queryLatestByResourceCode(String resourceCode) throws APIException {
        List<DlRescataResource> dlRescataResourceList = dlRescataResourceRepository.findByResourceCodeAndIsLatest(resourceCode, PublicConstant.YES);
        if (CollectionUtils.isEmpty(dlRescataResourceList)) {
            throw new APIException(-200, "该记录不存在");
        }
        if (dlRescataResourceList.size() > 1) {
            throw new APIException(-200, "最新版不唯一，数据有误");
        }
        if (dlRescataResourceList.size() == 1) {
            return dlRescataResourceList.get(0);
        }
        return null;
    }
}