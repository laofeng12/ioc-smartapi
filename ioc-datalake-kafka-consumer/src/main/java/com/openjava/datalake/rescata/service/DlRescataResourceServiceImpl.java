package com.openjava.datalake.rescata.service;

import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.rescata.domain.DlRescataResource;
import com.openjava.datalake.rescata.repository.DlRescataResourceRepository;
import com.openjava.datalake.rescata.vo.DlRescataResourceInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.ljdp.component.exception.APIException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author JiaHai
 * @Description 资源目录宽表 业务层接口实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DlRescataResourceServiceImpl implements DlRescataResourceService {

    @Resource
    DlRescataResourceRepository dlRescataResourceRepository;
    @Autowired
    private DataCodeService dataCodeService;
    @Override
    public DlRescataResource findByResourceId(Long resourceId) {
        DlRescataResource dlRescataResource = dlRescataResourceRepository.findByResourceId(resourceId);
        return dlRescataResource;
    }

    @Override
    public DlRescataResourceInfoVo findByResourceCode(String resourceCode) throws APIException {
        DlRescataResource dlRescataResource = this.queryLatestByResourceCode(resourceCode);
        return this.translateDlRescataResource(dlRescataResource);
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

    @Override
    public DlRescataResourceInfoVo translateDlRescataResource(DlRescataResource dlRescataResource) {
        if (null == dlRescataResource) {
            return null;
        }
        // 创建返回对象
        DlRescataResourceInfoVo dlRescataResourceInfoVo = new DlRescataResourceInfoVo();
        // 赋值属性信息
        BeanUtils.copyProperties(dlRescataResource, dlRescataResourceInfoVo);

        // 分库类别（dl.resource.repository.type）（1归集库、2中心库、3基础库、4主题课、5专题库）
        dlRescataResourceInfoVo.setRepositoryTypeString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(ResourceConstant.DL_RESOURCE_REPOSITORY_TYPE, dlRescataResource.getRepositoryType()))
                // 数据格式（dl.resource.type）（1数据库、2文本、3图片、4音频、5视频）
                .setResourceTypeString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(ResourceConstant.DL_RESOURCE_TYPE, dlRescataResource.getResourceType()))
                // 数据提供方式（dl.resource.data.provide.mode）（1库表挂载、2附件上传、3接口获取、4数据归集）
                .setSourceModeString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(ResourceConstant.DL_RESOURCE_DATA_PROVIDE_MODE, dlRescataResource.getSourceMode()))
                // 共享范围（dl.resource.share.scope）（1全市共享、2部门私有、3个人私有）
                .setOpenScopeString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(ResourceConstant.DL_RESOURCE_OPEN_SCOPE, dlRescataResource.getOpenScope()))
                // 更新周期（dl.resource.update.cycle）（1实时、2每日、3每周、4每月、5每季度、6每半年、7每年、8一次性、9不提供、10其他）
                .setUpdateCycleString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(ResourceConstant.DL_RESOURCE_UPDATE_CYCLE, dlRescataResource.getUpdateCycle()))
                // 资源安全级别（dl.resource.security.level）（1未分级、2内部、3秘密、4机密、5绝密）
                .setSecurityLevelString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(ResourceConstant.DL_RESOURCE_SECURITY_LEVEL, dlRescataResource.getSecurityLevel()))
                // 是否数据下载（public.YN）（1是、0否）
                .setIsDataDownloadString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(PublicConstant.PUBLIC_YN, dlRescataResource.getIsDataDownload()))
                // 是否数据下载审批（public.YN）（1是、0否）
                .setIsDataDownloadApprovalString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(PublicConstant.PUBLIC_YN, dlRescataResource.getIsDataDownloadApproval()))
                // 是否站内信息通知（public.YN）（1是，0否）
                .setIsInStationMsgNotifyString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(PublicConstant.PUBLIC_YN, dlRescataResource.getIsInStationMsgNotify()))
                // 是否短信提醒（public.YN）（1是，0否）
                .setIsSmsAlertsString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(PublicConstant.PUBLIC_YN, dlRescataResource.getIsSmsAlerts()))
                // 资源状态（dl.resource.resource.state）（1正常、2未上线、3已停更、4草稿、5已禁用、6待审批）
                .setResourceStateString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(ResourceConstant.DL_RESOURCE_RESOURCE_STATE, dlRescataResource.getResourceState()))
                // 是否为最新版（public.YN）（1是、0否）
                .setIsLatestString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(PublicConstant.PUBLIC_YN, dlRescataResource.getIsLatest()))
                // 数据领域（国标）（dl.resource.data.domain）
                .setDataDomainString(dataCodeService.findCodeNameByCodetypeAndCodeFromCache(ResourceConstant.DL_RESOURCE_DATA_DOMAIN, dlRescataResource.getDataDomain()));

        return dlRescataResourceInfoVo;
    }
}
