package com.openjava.datalake.push.query;

import lombok.Data;
import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 查询对象
 * @author ljc
 *
 */
@Data
public class DlApiPushDBParam extends RoDBQueryParam {
	private Long eq_id;//主键 --主键查询
	
	private String like_resourceName;//资源目录名称 like ?
	private Long eq_apiState;//接口状态(0-有效 1-无效) = ?
	private Long eq_isCreateApi;//是否生成接口(0-是 1-否) = ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_updateTime;//最近更新时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_updateTime;//最近更新时间 >= ?
	private String eq_createAccount;
	

}