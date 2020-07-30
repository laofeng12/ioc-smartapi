package com.openjava.datalake.rescata.service;

import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.domain.DlRescataResource;
import com.openjava.datalake.rescata.domain.DlRescataStrucPermi;
import com.openjava.datalake.rescata.repository.DlRescataResourceRepository;
import com.openjava.datalake.rescata.repository.DlRescataStructurePermissionRepository;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资源目录字段权限表业务层
 * @author xjd
 *
 */
@Service
@Transactional
public class DlRescataStructurePermissionServiceImpl implements DlRescataStructurePermissionService {
	
	@Resource
	private DlRescataResourceRepository dlRescataResourceRepository;
	@Resource
	private DlRescataResourceService dlRescataResourceService;
	@Resource
	private DlRescataColumnService dlRescataColumnService;
	@Resource
	private DlRescataStructurePermissionRepository dlRescataStructurePermissionRepository;

	@Override
	public Map<Long, DlRescataStrucPermi> findAlreadyExistPermittedColumn(String resourceCode, String userAccount) throws APIException {
		DlRescataResource rescataResource = dlRescataResourceService.queryLatestByResourceCode(resourceCode);
		return this.findAlreadyExistPermittedColumn(rescataResource.getResourceId(), userAccount);
	}

	@Override
	public Map<Long, DlRescataStrucPermi> findAlreadyExistPermittedColumn(Long resourceId, String userAccount) throws APIException {
		List<DlRescataColumn> dlRescataColumns = dlRescataColumnService.findByResourceId(resourceId);
		return this.findAlreadyExistPermittedColumn(dlRescataColumns, userAccount);
	}
    @Override
    public Map<Long, DlRescataStrucPermi> findAlreadyExistPermittedColumn(List<DlRescataColumn> dlRescataColumns, String userAccount){
		Set<Long> structureIdSet = dlRescataColumns.stream().map(DlRescataColumn::getStructureId).collect(Collectors.toSet());
		return this.findAlreadyExistPermittedColumn(structureIdSet, userAccount);
	}

    @Override
    public Map<Long, DlRescataStrucPermi> findAlreadyExistPermittedColumn(Set<Long> structureIdSet, String userAccount){
		List<DlRescataStrucPermi> areadyExistPermissionColumnList = dlRescataStructurePermissionRepository.findByStructureIdInAndOwnerAccount(structureIdSet, userAccount);
		if (areadyExistPermissionColumnList != null) {
			Map<Long, DlRescataStrucPermi> permittedColumnMap = areadyExistPermissionColumnList.stream().collect(Collectors.toMap(DlRescataStrucPermi::getStructureId, Function.identity()));
			return permittedColumnMap;
		} else {
			return Collections.EMPTY_MAP;
		}
	}

}
