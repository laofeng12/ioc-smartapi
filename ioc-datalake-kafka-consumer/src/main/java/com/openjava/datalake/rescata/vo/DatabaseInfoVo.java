package com.openjava.datalake.rescata.vo;

import com.openjava.datalake.util.JdbcUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 数据库连接信息包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DatabaseInfoVo implements Serializable {
    private static final long serialVersionUID = 428826517592959082L;

    @ApiModelProperty("IP")
    private String ip;
    @ApiModelProperty("端口号")
    private String port;
    @ApiModelProperty("数据库名")
    private String databaseName;
    @ApiModelProperty("schema")
    private String schema;

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("数据库类型")
    private Long databaseType;

    @ApiModelProperty("JDBC尾部的其他参数")
    private String otherParam;

    @ApiModelProperty("JdbcURL")
    private String jdbcUrl;

    @Transient
    @ApiModelProperty("当前数据库表名（汇聚使用）")
    private String tableName;

    @Transient
    @ApiModelProperty("当前数据库表注释（汇聚使用）")
    private String tableComment;

    public DatabaseInfoVo(String ip, String port, String databaseName, String schema, String username, String password, Long databaseType, String otherParam) {
        this.ip = ip;
        this.port = port;
        this.databaseName = databaseName;
        this.schema = schema;
        this.username = username;
        this.password = password;
        this.databaseType = databaseType;
        this.otherParam = otherParam;
    }

    public String getJdbcUrl() {
        if (this.jdbcUrl == null) {
            String jdbcUrl = JdbcUtils.generateJdbcUrl(databaseType, this.getIp(), this.getPort(), this.getDatabaseName(), this.getSchema());
            this.jdbcUrl = jdbcUrl;
        }
        return jdbcUrl;
    }
}
