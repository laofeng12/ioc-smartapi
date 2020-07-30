package com.openjava.datalake.subscribe.service;

import com.openjava.datalake.base.dto.ResourceFileOutputDTO;
import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.insensitives.service.DlInsensitivesRuleService;
import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.domain.DlRescataResource;
import com.openjava.datalake.rescata.domain.DlRescataResourcePermission;
import com.openjava.datalake.rescata.domain.DlRescataStrucPermi;
import com.openjava.datalake.rescata.service.DlRescataResourceService;
import com.openjava.datalake.rescata.service.DlResourceUnstructuredFileService;
import com.openjava.datalake.rescata.vo.TableData;
import com.openjava.datalake.subscribe.domain.DlSubscribeColumn;
import com.openjava.datalake.subscribe.domain.DlSubscribeUnstrucPermi;
import com.openjava.datalake.subscribe.domain.vo.PermiFilterResourceInfoParamVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订阅资源目录业务层
 * @author xjd
 *
 */
@Service
@Log4j2
@Transactional
public class DlSubscribeCatalogFormServiceImpl implements DlSubscribeCatalogFormService {

	private static Logger LOG = LoggerFactory.getLogger(DlSubscribeCatalogFormServiceImpl.class);
	@Resource
	private DlRescataResourceService dlRescataResourceService;
	@Resource
	private DlInsensitivesRuleService dlInsensitivesRuleService;
	@Resource
	private DlResourceUnstructuredFileService dlResourceUnstructuredFileService;
	/**
	 * 优化后，不重复去数据库查询，用之前已经查过的
	 * @param tableData
	 * @param userAccount
	 * @param permiFilterResourceInfoParamVo 存放资源目录相关
	 * @return
	 */
	@Override
	public TableData permissionFilter2(TableData tableData, String userAccount, PermiFilterResourceInfoParamVo permiFilterResourceInfoParamVo) {
		@NotNull DlRescataResource dlRescataResource = permiFilterResourceInfoParamVo.getDlRescataResource();
		if (this.persionalResSkipPermiFilter(tableData, userAccount, dlRescataResource)) {
			return tableData;
		}
		List<DlRescataColumn> dlRescataColumnList = tableData.getDlRescataColumnList();
		if (CollectionUtils.isEmpty(dlRescataColumnList)) {
			return new TableData(Collections.EMPTY_LIST);
		}
		//获取已经申请通过的权限(已走缓存)
//		List<Long> structureIds = dlRescataColumnList.stream().map(DlRescataColumn::getStructureId).collect(Collectors.toList());
//		List<DlRescataStrucPermi> structurePermissions = dlRescataStructurePermissionRepository.findByStructureIdInAndOwnerAccount(structureIds, userAccount);

		//有权限资源目录字段
		@NotNull List<DlRescataColumn> allRescataColumnList = permiFilterResourceInfoParamVo.getAllRescataColumnList();
		@NotNull List<DlRescataStrucPermi> structurePermissions = permiFilterResourceInfoParamVo.getStructurePermissions();
		List<DlRescataColumn> permittedColumnList = new ArrayList<>();
		structurePermissions.forEach(permission -> {
			for(DlRescataColumn column : allRescataColumnList){
				if(column.getStructureId().equals(permission.getStructureId())){
					permittedColumnList.add(column);
					break;
				}
			}
		});
		tableData.setStructurePermissions(structurePermissions);
		permissionFilterResourceId(tableData, permittedColumnList, userAccount);
		return tableData;
	}

	/**
	 * 判断是否跳过个人目录不做权限过滤
	 * @param tableData
	 * @param userAccount
	 * @param dlRescataResource
	 * @return
	 */
	private boolean persionalResSkipPermiFilter(TableData tableData, String userAccount, DlRescataResource dlRescataResource) {
		if (dlRescataResource == null) {
			List<DlRescataColumn> dlRescataColumnList = tableData.getDlRescataColumnList();
			if (CollectionUtils.isNotEmpty(dlRescataColumnList)) {
				Long resourceId = dlRescataColumnList.get(0).getResourceId();
				if (resourceId != null) {
					try {
						// 若没传，拿到资源目录
						dlRescataResource = dlRescataResourceService.queryLatestByResourceId(resourceId);
					} catch (APIException e) {
						// 拿不到资源目录，就不做个人目录跳过权限过滤的操作
						LOG.error(e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		if (dlRescataResource != null) {
			String createAccount = dlRescataResource.getCreateAccount();
			Long openScope = dlRescataResource.getOpenScope();
			// 自己的个人目录不筛选过滤数据，全部返回，设置为全部权限，升级成部门目录就会失效
			if (ResourceConstant.RESOURCE_OPEN_SCOPE_PRIVATE.equals(openScope) && StringUtils.equals(userAccount, createAccount)) {
				tableData.setPermissionLevel(PublicConstant.RESOURCE_PERMISSION_LEVEL_ALL);
				return true;
			}
		}
		return false;
	}


	/**
	 * 实际过滤数据操作
	 * 包含逻辑计算permissionLevel的值
	 * @param tableData
	 * @param permittedColumnList
	 * @param userAccount
	 */
	private void permissionFilterResourceId(TableData tableData, List<DlRescataColumn> permittedColumnList, String userAccount) {
		if (tableData.getPermissionLevel() == null) {
			// 先设置全部，再从遍历中判断是否部分或者一个都没有
			tableData.setPermissionLevel(PublicConstant.RESOURCE_PERMISSION_LEVEL_ALL);
		}
		List<DlRescataColumn> dlRescataColumnList = tableData.getDlRescataColumnList();
		List<Object[]> data = tableData.getData();
		if (CollectionUtils.isEmpty(permittedColumnList)) {
			tableData.setData(Collections.EMPTY_LIST);
			tableData.setMessage("无查看权限，请先订阅");
			tableData.setCode(HttpStatus.UNAUTHORIZED.value());
			tableData.setPermissionLevel(PublicConstant.RESOURCE_PERMISSION_LEVEL_NONE);
			// 没有权限直接返回无权的提示
//			for (int rowNum = 0; rowNum < data.size(); rowNum++) {
//				Object[] oneRow = data.get(rowNum);
//				for (int columnNumR = oneRow.length - 1; columnNumR >= 0; columnNumR--) {
////					oneRow[columnNumR] = "没有权限";
////					oneRow[columnNumR] = "--";
////					oneRow[columnNumR] = "";
//					oneRow[columnNumR] = null;
//				}
//
//			}
			return;
		}
		Map<Long, DlRescataColumn> permittedColumnMap = permittedColumnList.stream().collect(Collectors.toMap(DlRescataColumn::getStructureId, Function.identity()));
		Set<Long> permittedColumnIds = permittedColumnMap.keySet();
		// 置空没权限的
		for (int columnNum = 0; columnNum < dlRescataColumnList.size(); columnNum++) {
			DlRescataColumn dlRescataColumn = dlRescataColumnList.get(columnNum);
//			boolean contains = permittedColumnIds.contains(dlRescataColumn.getStructureId());
			DlRescataColumn permittedColumn = permittedColumnMap.get(dlRescataColumn.getStructureId());
			if (permittedColumn == null) {
				for (Object[] datum : data) {
//					datum[columnNum] = "没有权限";
					datum[columnNum] = null;
					// 有一个没有权限就是部分，因为上面已经判断了是否全部都没有，能下来的都是至少有一个权限的
					if (!PublicConstant.RESOURCE_PERMISSION_LEVEL_NONE.equals(tableData.getPermissionLevel())) {
						tableData.setPermissionLevel(PublicConstant.RESOURCE_PERMISSION_LEVEL_PART);
					}
				}
			}
		}

		// 过虑脱敏
		dlInsensitivesRuleService.inSensitivesFilter(tableData, userAccount);
		// 过滤加密
		dlInsensitivesRuleService.encryptFilter(tableData, userAccount);

	}

	/**
	 * 计算权限等级
	 * @param dlRescataColumnList
	 * @param areadyExistPermittedColumn
	 * @return
	 */
	@Override
	public Long calcPermitLevel(List<DlRescataColumn> dlRescataColumnList, Map<Long, DlRescataStrucPermi> areadyExistPermittedColumn) {
		List<DlSubscribeColumn> subscribeColumns = new ArrayList<>();

		Iterator<Map.Entry<Long, DlRescataStrucPermi>> iterator = areadyExistPermittedColumn.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Long, DlRescataStrucPermi> next = iterator.next();
			DlRescataStrucPermi dlRescataStrucPermi = next.getValue();
			Long structureId = next.getKey();

			DlSubscribeColumn dlSubscribeColumn = new DlSubscribeColumn();
			dlSubscribeColumn.setIsApproveSensitive(dlRescataStrucPermi.getIsSensitived());
			dlSubscribeColumn.setIsApproveDecryption(dlRescataStrucPermi.getIsDecryption());
			dlSubscribeColumn.setIsApproveViewable(PublicConstant.YES);
			dlSubscribeColumn.setStructureId(structureId);

			subscribeColumns.add(dlSubscribeColumn);
		}

		return this.calcPermitLevel(dlRescataColumnList, subscribeColumns);
	}
	@Override
	public Long calcPermitLevel(List<DlRescataColumn> dlRescataColumnList, List<DlSubscribeColumn> subscribeColumns) {
		Map<Long, DlSubscribeColumn> dlSubscribeColumnMap = subscribeColumns.stream().collect(Collectors.toMap(DlSubscribeColumn::getStructureId, Function.identity()));
		Long permissionLevel = null;
		boolean allMatchAllPermission = true;
		boolean anyMatchOnePermission = false;
		for (DlRescataColumn dlRescataColumn : dlRescataColumnList) {
			Long structureId = dlRescataColumn.getStructureId();
			DlSubscribeColumn dlSubscribeColumn = dlSubscribeColumnMap.get(structureId);
			if (dlSubscribeColumn != null) {

				// 对比权限是否都已经获取 可视、加密、脱敏
//				boolean approveView = !PublicConstant.COLUMN_OPEN_SCOPE_NOT_OPEN.equals(columnViewableType) && !PublicConstant.NO.equals(isApproveViewable);
				Long isApproveViewable = dlSubscribeColumn.getIsApproveViewable();
				Long columnViewableType = dlRescataColumn.getColumnOpenScope();
				boolean approveView;
				if (isApproveViewable != null) {
					approveView = PublicConstant.YES.equals(isApproveViewable);
				} else if (!PublicConstant.COLUMN_OPEN_SCOPE_NOT_OPEN.equals(columnViewableType)) {
					approveView = true;
				} else {
					approveView = false;
				}
				Long isApproveDecryprion = dlSubscribeColumn.getIsApproveDecryption();
				Long encryptionState = dlRescataColumn.getIsEncrypt();
//				boolean approveDecryprion = PublicConstant.NO.equals(encryptionState) && !PublicConstant.NO.equals(isApproveDecryption);
				boolean approveDecryprion;
				if (isApproveDecryprion != null) {
					approveDecryprion = PublicConstant.YES.equals(isApproveDecryprion);
				} else if (!PublicConstant.YES.equals(encryptionState)) {
					approveDecryprion = true;
				} else {
					approveDecryprion = false;
				}
				Long isApproveSensitive = dlSubscribeColumn.getIsApproveSensitive();
				Long insensitivesState = dlRescataColumn.getIsDesensitization();
//				boolean approveSensitive = PublicConstant.NO.equals(insensitivesState) && !PublicConstant.NO.equals(isApproveSensitive);
				boolean approveSensitive;
				if (isApproveSensitive != null) {
					approveSensitive = PublicConstant.YES.equals(isApproveSensitive);
				} else if (!PublicConstant.YES.equals(insensitivesState)) {
					approveSensitive = true;
				} else {
					approveSensitive = false;
				}

				if (!(approveView && approveDecryprion && approveSensitive)) {
					allMatchAllPermission = false;
				}
				if (approveView) {
					anyMatchOnePermission = true;
				}
			} else {
				allMatchAllPermission = false;
			}
		}
		if (dlRescataColumnList.size() != subscribeColumns.size()) {
			allMatchAllPermission = false;
		}
		if (allMatchAllPermission) {
			permissionLevel = PublicConstant.RESOURCE_PERMISSION_LEVEL_ALL;
		} else if (anyMatchOnePermission) {
			permissionLevel = PublicConstant.RESOURCE_PERMISSION_LEVEL_PART;
		} else {
			permissionLevel = PublicConstant.RESOURCE_PERMISSION_LEVEL_NONE;
		}
		return permissionLevel;
	}

	@Override
	public Long calcUnstructurePermitLevel(Long resourceId, String resourceCode, Map<Long, DlSubscribeUnstrucPermi> alreadyExistPermittedUnstructrue) throws APIException {
		Long permissionLevel = null;
		boolean allMatchAllPermission = true;
		boolean anyMatchOnePermission = false;
//		List<ResourceFileOutputDTO> resourceFileOutputDTOS = dlResourceUnstructuredFileService.findByResourceId(resourceId);
		List<ResourceFileOutputDTO> resourceFileOutputDTOS = dlResourceUnstructuredFileService.getFileListByResourceCode(resourceCode);
		for (ResourceFileOutputDTO resourceFileOutputDTO : resourceFileOutputDTOS) {
			Long unstructureId = resourceFileOutputDTO.getId();
			DlSubscribeUnstrucPermi dlSubscribeUnstrucPermi = alreadyExistPermittedUnstructrue.get(unstructureId);
			if (dlSubscribeUnstrucPermi == null) {
				allMatchAllPermission = false;
			} else {
				anyMatchOnePermission = true;
			}
		}

		if (allMatchAllPermission) {
			permissionLevel = PublicConstant.RESOURCE_PERMISSION_LEVEL_ALL;
		} else if (anyMatchOnePermission) {
			permissionLevel = PublicConstant.RESOURCE_PERMISSION_LEVEL_PART;
		} else {
			permissionLevel = PublicConstant.RESOURCE_PERMISSION_LEVEL_NONE;
		}

		return permissionLevel;
	}
}
