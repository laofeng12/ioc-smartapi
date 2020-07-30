package com.openjava.datalake.common.responseBody;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openjava.datalake.common.DtsConstants;
import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.rescata.vo.DatabaseInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@ApiModel("数据源链接信息")
@Data
@Accessors(chain = true)
public class DtsDataSourceConnInfoResp implements Serializable {
	
	@ApiModelProperty("数据源ID")
	private String datasourceId;
	
	@ApiModelProperty("数据库类型（ 0:Oracle,1:MySql高版本 2：Mysql低版本 3:PostgreSQL  4:hive  5:SQL Server 6:华为hive，7:GAUSSDB）")
	private Integer databaseType;

	@ApiModelProperty("数据库类型（ 0:Oracle,1:MySql高版本 2：Mysql低版本 3:PostgreSQL  4:hive  5:SQL Server 6:华为hive，7:GAUSSDB）名称")
	private String databaseTypeName;
	
	@ApiModelProperty("集群连接地址URL")
	private String url;
	
	@ApiModelProperty("主机IP")
	private String hostIp;
	
	@ApiModelProperty("服务名")
	private String serviceName;
	
	@ApiModelProperty("数据库名")
	private String databaseName;
	
	@ApiModelProperty("模式名称")
	private String schemaName;
	
	@ApiModelProperty("端口号")
	private Long port;
	
	@ApiModelProperty("用户名")
	private String username;

	@ApiModelProperty("委托用户")
	private String principal;
	
	@ApiModelProperty("密码")
	private String password;

	@ApiModelProperty("安全认证类型(USER-PWD，KERBEROS，KERBEROS-HW）")
	private String securityType;

	@ApiModelProperty("keytab文件")
	private String keytab;

	@ApiModelProperty("krb5.conf文件")
	private String krb5conf;
	
	@ApiModelProperty("hdfs连接地址")
	private String hdfsUrl;

	@ApiModelProperty("表关联的hdfs文件路径")
	private String hdfsPath;

	@ApiModelProperty("数据库描述")
	private String description;

	@ApiModelProperty("数据库用途（1、数据源；2、目标库）")
	private Integer databaseUse;

	@ApiModelProperty("数据库用途名称（1、数据源；2、目标库）")
	private String databaseUseName;

	@ApiModelProperty("关联局系统id")
	private String systemIds;

	@ApiModelProperty("关联局系统名字")
	private String systemNames;

	@ApiModelProperty("连通状态（1、连通；2、未连通）")
	private Integer linkStatus;

	@ApiModelProperty("连通状态名称（1、连通；2、未连通）")
	private String linkStatusName;

	@ApiModelProperty("变更提醒（0、不提醒；1、提醒）")
	private Integer changeRemind;

	@ApiModelProperty("数据库来源")
	private String databaseSource;

	@ApiModelProperty("数据库来源名称")
	private String databaseSourceName;

	@ApiModelProperty("创建人ID")
	private String createId;

	@ApiModelProperty("创建人名称")
	private String createName;

	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	@ApiModelProperty("修改人ID")
	private String modifyId;

	@ApiModelProperty("修改人名称")
	private String modifyName;

	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;

	@ApiModelProperty("来源业务ID")
	private String businessId;

	@ApiModelProperty("业务系统ID（DTS_INTEGRATION:数据汇聚平台）")
	private String systemId = "DTS_INTEGRATION";

	private String dbName;

    public String getJdbcDriverClass(){
		if (databaseType == null) {
			return null;
		}

		if (databaseType.equals(DtsConstants.DATABASE_TYPE_ORACLE)) {
			return "oracle.jdbc.OracleDriver";
		} else if (databaseType.equals(DtsConstants.DATABASE_TYPE_MYSQL_NEW)) {
			return "com.mysql.cj.jdbc.Driver";
		} else if(databaseType.equals(DtsConstants.DATABASE_TYPE_MYSQL_OLD)){
			return "com.mysql.jdbc.Driver";
		} else if(databaseType.equals(DtsConstants.DATABASE_TYPE_POSTGRES)){
			return "org.postgresql.Driver";
		} else if(databaseType.equals(DtsConstants.DATABASE_TYPE_HIVE)
			|| databaseType.equals(DtsConstants.DATABASE_TYPE_HIVE_HUAWEI)){
			return "org.apache.hive.jdbc.HiveDriver";
		}
		return null;
	}

	public String getJDBCUrl(String jdbcParams) throws APIException {
		StringBuilder urlDest = null;
		if (DtsConstants.DATABASE_TYPE_ORACLE.equals(this.getDatabaseType())) {
			urlDest = new StringBuilder("jdbc:oracle:thin:@//");
			urlDest.append(this.getHostIp()).append(":").append(this.getPort()).append("/").append(this.getDatabaseName());
		} else if (DtsConstants.DATABASE_TYPE_MYSQL_NEW.equals(this.getDatabaseType())
				|| DtsConstants.DATABASE_TYPE_MYSQL_OLD.equals(this.getDatabaseType())) {
			urlDest = new StringBuilder("jdbc:mysql://");
			urlDest.append(this.getHostIp()).append(":").append(this.getPort()).append("/").append(this.getDatabaseName()).append("?useUnicode=true&characterEncoding=utf-8&useSSL=false");
		} else if(DtsConstants.DATABASE_TYPE_POSTGRES.equals(this.getDatabaseType())){
			urlDest = new StringBuilder("jdbc:postgresql://");
			urlDest.append(this.getHostIp()).append(":").append(this.getPort()).append("/").append(this.getDatabaseName());
			if(StringUtils.isNotBlank(this.getSchemaName())){
				//searchpath -> currentSchema
				urlDest.append("?currentSchema=").append(this.getSchemaName());
			}
		} else if(DtsConstants.DATABASE_TYPE_HIVE.equals(this.getDatabaseType())
			|| DtsConstants.DATABASE_TYPE_HIVE_HUAWEI.equals(this.getDatabaseType())){
			urlDest = new StringBuilder("jdbc:hive2://");
			urlDest.append(this.getHostIp());
			if(this.getPort() != null && this.getPort().intValue() > 0){
				urlDest.append(":").append(this.getPort());
			}
			urlDest.append("/");
			if (this.getDatabaseName() != null) {
				urlDest.append(this.getDatabaseName());
			}
			if (jdbcParams != null) {
				urlDest.append(jdbcParams);
			}
		} else if(DtsConstants.DATABASE_TYPE_TXT.equals(this.getDatabaseType())){
			urlDest = new StringBuilder();
		}else{
			throw new APIException(1001, "暂不支持此数据类型");
		}
		return urlDest.toString();
	}

	public DatabaseInfoVo converToDatabaseInfoVo(DatabaseInfoVo databaseInfoVo) {
		if (databaseInfoVo == null) {
			databaseInfoVo = new DatabaseInfoVo();
		}
		databaseInfoVo.setIp(this.hostIp)
				.setPort(String.valueOf(this.port))
				.setDatabaseName(this.databaseName)
				.setSchema(this.schemaName)
				.setUsername(this.username)
				.setPassword(this.password)
				.setJdbcUrl(this.url);

		Long dataLakeDatabaseType = null;
		if (DtsConstants.DATABASE_TYPE_ORACLE.equals(this.databaseType)) {
			dataLakeDatabaseType = ResourceConstant.DATABASE_TYPE_ORACLE;
		} else if (DtsConstants.DATABASE_TYPE_MYSQL_NEW.equals(this.databaseType)
				|| DtsConstants.DATABASE_TYPE_MYSQL_OLD.equals(this.databaseType)) {
			dataLakeDatabaseType = ResourceConstant.DATABASE_TYPE_MYSQL;
		} else if (DtsConstants.DATABASE_TYPE_HIVE.equals(this.databaseType)
				|| DtsConstants.DATABASE_TYPE_HIVE_HUAWEI.equals(this.databaseType)) {
			dataLakeDatabaseType = ResourceConstant.DATABASE_TYPE_HIVE;
		} else if (DtsConstants.DATABASE_TYPE_POSTGRES.equals(this.databaseType)) {
			dataLakeDatabaseType = ResourceConstant.DATABASE_TYPE_MPP;
		}
		databaseInfoVo.setDatabaseType(dataLakeDatabaseType);
		return databaseInfoVo;
	}

}