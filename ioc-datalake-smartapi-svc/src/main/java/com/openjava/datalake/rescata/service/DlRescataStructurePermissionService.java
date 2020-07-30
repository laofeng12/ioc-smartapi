package com.openjava.datalake.rescata.service;

import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.domain.DlRescataStrucPermi;
import org.ljdp.component.exception.APIException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 资源目录字段权限表业务层接口
 * @author xjd
 *
 */
public interface DlRescataStructurePermissionService {

	/**
	 * 查找现有的信息项权限情况
	 * key是信息项ID structureId
	 * @param resourceCode
	 * @param userAccount
	 * @return
	 * @throws APIException
	 */
	Map<Long, DlRescataStrucPermi> findAlreadyExistPermittedColumn(String resourceCode, String userAccount) throws APIException;

	Map<Long, DlRescataStrucPermi> findAlreadyExistPermittedColumn(Long resourceId, String userAccount) throws APIException;

	Map<Long, DlRescataStrucPermi> findAlreadyExistPermittedColumn(List<DlRescataColumn> dlRescataColumns, String userAccount);

	/**
	 * 根据用户账号查询字段id对应的权限map
	 * key: structureId
	 * @param structureIdSet
	 * @param userAccount
	 * @return
	 */
	Map<Long, DlRescataStrucPermi> findAlreadyExistPermittedColumn(Set<Long> structureIdSet, String userAccount);

}
