package com.openjava.datalake.subscribe.service;

import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.domain.DlRescataStrucPermi;
import com.openjava.datalake.rescata.vo.TableData;
import com.openjava.datalake.subscribe.domain.DlSubscribeColumn;
import com.openjava.datalake.subscribe.domain.DlSubscribeUnstrucPermi;
import com.openjava.datalake.subscribe.domain.vo.PermiFilterResourceInfoParamVo;
import org.ljdp.component.exception.APIException;

import java.util.List;
import java.util.Map;

/**
 * 订阅资源目录业务层接口
 * @author xjd
 *
 */
public interface DlSubscribeCatalogFormService {
	TableData permissionFilter2(TableData tableData, String userAccount, PermiFilterResourceInfoParamVo permiFilterResourceInfoParamVo);

	/**
	 * 计算权限等级
	 * @param dlRescataColumnList
	 * @param areadyExistPermittedColumn
	 * @return
	 */
	Long calcPermitLevel(List<DlRescataColumn> dlRescataColumnList, Map<Long, DlRescataStrucPermi> areadyExistPermittedColumn);

	Long calcPermitLevel(List<DlRescataColumn> dlRescataColumnList, List<DlSubscribeColumn> subscribeColumns);

	/**
	 * 计算非结构化资源目录的权限等级
	 *
	 * @param resourceId
	 * @param resourceCode
	 * @param alreadyExistPermittedUnstructrue
	 * @return
	 */
	Long calcUnstructurePermitLevel(Long resourceId, String resourceCode, Map<Long, DlSubscribeUnstrucPermi> alreadyExistPermittedUnstructrue) throws APIException;
}
