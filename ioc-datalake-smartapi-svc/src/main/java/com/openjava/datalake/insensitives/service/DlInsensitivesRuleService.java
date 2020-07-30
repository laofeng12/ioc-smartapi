package com.openjava.datalake.insensitives.service;

import com.openjava.datalake.insensitives.domain.DlInsensitivesRule;
import com.openjava.datalake.rescata.vo.TableData;

/**
 * 脱敏规则库业务层接口
 * @author xjd
 *
 */
public interface DlInsensitivesRuleService {

    TableData encryptFilter(TableData tableData, String userAccount);

    /**
	 * 根据column查找脱敏的规则，设置字段为脱敏后的字段，并且返回
	 * 传入已经获取到数据的VO
	 * @param tableDataVo
	 */
	TableData inSensitivesFilter(TableData tableDataVo, String userAccount);

	DlInsensitivesRule get(Long id);

}
