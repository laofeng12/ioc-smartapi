package com.openjava.datalake.rescata.service;

import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.domain.DlRescataResource;
import com.openjava.datalake.rescata.domain.DlRescataResourcePermission;
import com.openjava.datalake.rescata.domain.DlRescataStrucPermi;
import com.openjava.datalake.rescata.repository.DlRescataResourcePermissionRepository;
import com.openjava.datalake.rescata.repository.DlRescataResourceRepository;
import com.openjava.datalake.subscribe.domain.DlSubscribeUnstrucPermi;
import com.openjava.datalake.subscribe.service.DlSubscribeCatalogFormService;
import com.openjava.datalake.subscribe.service.DlSubscribeUnstrucPermiService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.secure.sso.SsoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资源目录权限表业务层
 * @author xjd
 *
 */
@Service
@Transactional
public class DlRescataResourcePermissionServiceImpl implements DlRescataResourcePermissionService {

	private static Logger LOGGER = LoggerFactory.getLogger(DlRescataResourcePermissionServiceImpl.class);

	@Resource
	private DlRescataResourcePermissionRepository dlRescataResourcePermissionRepository;
	@Resource
	private DlRescataStructurePermissionService dlRescataStructurePermissionService;
	@Resource
	private DlSubscribeUnstrucPermiService dlSubscribeUnstrucPermiService;
	@Resource
	private DlRescataResourceService dlRescataResourceService;
	@Resource
	private DlRescataColumnService dlRescataColumnService;
	@Resource
	private DlSubscribeCatalogFormService dlSubscribeCatalogFormService;
	@Resource
	private DlRescataResourceRepository dlRescataResourceRepository;

	@Override
    public Long getResourcePermissionLevel(Long resourceId, String userAccount) throws APIException {
		DlRescataResource byResourceId = dlRescataResourceService.getByResourceId(resourceId);
		String resourceCode = byResourceId.getResourceCode();
		return this.getResourcePermissionLevel(resourceCode, userAccount);
	}

	@Override
	public Long getResourcePermissionLevel(String resourceCode, String userAccount) throws APIException {
		DlRescataResource rescataResource = dlRescataResourceService.queryLatestByResourceCode(resourceCode);
		// TODO 自己的个人目录直接全部权限
		if (ResourceConstant.RESOURCE_OPEN_SCOPE_PRIVATE.equals(rescataResource.getOpenScope())
				&& StringUtils.equals(rescataResource.getCreateAccount(), userAccount)) {
			return PublicConstant.RESOURCE_PERMISSION_LEVEL_ALL;
		}
		Long resourceId = rescataResource.getResourceId();
		List<DlRescataColumn> dlRescataColumns = dlRescataColumnService.findByResourceId(rescataResource.getResourceId());

		Long resourceType = rescataResource.getResourceType();
		if (ResourceConstant.RESOURCE_TYPE_DATABASE.equals(resourceType)) {
			Map<Long, DlRescataStrucPermi> areadyExistPermittedColumn = dlRescataStructurePermissionService.findAlreadyExistPermittedColumn(dlRescataColumns, userAccount);
			if (CollectionUtils.isNotEmpty(areadyExistPermittedColumn.keySet())) {
				// 通过当前对信息项的权限计算权限等级
				Long permissionLevel = dlSubscribeCatalogFormService.calcPermitLevel(dlRescataColumns, areadyExistPermittedColumn);
				return permissionLevel;
			} else {
				return PublicConstant.RESOURCE_PERMISSION_LEVEL_NONE;
			}
		} else {
			// 非结构化权限等级判断
			Map<Long, DlSubscribeUnstrucPermi> alreadyExistPermittedUnstructrue = dlSubscribeUnstrucPermiService.findAlreadyExistPermittedUnstructrue(resourceCode, userAccount);
			if (alreadyExistPermittedUnstructrue != null && !alreadyExistPermittedUnstructrue.isEmpty()) {
				// 通过当前对信息项的权限计算权限等级
				Long permissionLevel = dlSubscribeCatalogFormService.calcUnstructurePermitLevel(resourceId, resourceCode, alreadyExistPermittedUnstructrue);
				return permissionLevel;
			} else {
				return PublicConstant.RESOURCE_PERMISSION_LEVEL_NONE;
			}

		}

	}
}
