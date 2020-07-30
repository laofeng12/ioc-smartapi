package com.openjava.datalake.rescata.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 资源目录与数据库连接关系表
 */
@Entity
@Table(name = "DL_RESCATA_RESOURCE_DATABASE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DlRescataResourceDatabase implements Serializable {
    private static final long serialVersionUID = 7985544050856893331L;

    @Id
    @ApiModelProperty("主键ID")
    private Long resourceDatabaseRefId;
    @ApiModelProperty("资源目录信息资源编码")
    private String resourceCode;
    @ApiModelProperty("数据库编码主键ID")
    private Long databaseId;

}
