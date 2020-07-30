package com.openjava.datalake.rescata.service;

import org.ljdp.component.exception.APIException;

/**
 * 资源目录权限表业务层接口
 *
 * @author xjd
 */
public interface DlRescataResourcePermissionService {

    Long getResourcePermissionLevel(Long resourceId, String userAccount) throws APIException;

    /**
     * 拿PermitLevel不能从dlRescataResourcePermission.getPermissionLevel()的持久化对象中拿
     * 因为 资源目录版本更新，有可能改动信息项，信息项的权限配置改动了就要重新计算用户对资源目录的权限等级（全部、部分、没有）
     * 例如字段本来是没有加密的，用户对于该资源目录是“全部权限”，版本变更后，字段变成加密了，理应要更新权限等级为“部分”。
     *
     * 已经在资源目录更新方法后添加继承旧版本权限的方法，dlRescataResourcePermission.getPermissionLevel()持久化数据有效
     * dlSubscribeCatalogFormService.extendOldVersionPermisssion(user, resourceId);
     * @param resourceCode
     * @param userAccount
     * @return
     * @throws APIException
     */
    Long getResourcePermissionLevel(String resourceCode, String userAccount) throws APIException;

}
