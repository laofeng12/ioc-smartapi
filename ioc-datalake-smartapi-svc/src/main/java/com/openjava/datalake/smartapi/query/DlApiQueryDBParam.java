package com.openjava.datalake.smartapi.query;

import lombok.Data;
import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author zjf
 *
 */
@Data
public class DlApiQueryDBParam extends RoDBQueryParam {
	private Long eq_queryid;//查询ID --主键查询

	private String like_queryName;//接口名称 like ?
	private String like_resourceName;//资源目录名称 like ?

	private String eq_createId;//用户ID
	private String eq_createBy;//用户名
	private Long eq_deleteMark;//是否删除：0正常、1删除
	private Long eq_validMark;//是否有效：0无效、1有效
	private String eq_createUserAccount;
}