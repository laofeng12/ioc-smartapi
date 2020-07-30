package com.openjava.datalake.push.dao;

import com.openjava.datalake.push.domain.DlApiPush;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DlApiPushMapper {
	/**
	 * 资源目录表、提交接口管理表左连接查询，返回集合
	 * @param resourceName
	 * @param isCreateApi
	 * @param apiState
	 * @param endRow
	 * @param StartRow
	 * @return
	 */
	List<DlApiPush> queryDlApiPushList(@Param("resourceName") String resourceName, Long isCreateApi, Long apiState, String account, int endRow, int StartRow);
	int countDlApiPushList(@Param("resourceName") String resourceName, Long isCreateApi, Long apiState, String account);

}
