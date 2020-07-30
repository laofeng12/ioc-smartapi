package com.openjava.datalake.rescata.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 资源目录关联数据源（汇聚）（挂载专用） 实体类
 */
@Entity
@Table(name = "DL_RESOURCE_DATASOURCE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DlResourceDatasource implements Serializable {
    private static final long serialVersionUID = 1268334345612920471L;

    @Id
    @ApiModelProperty("主键ID")
    private Long primaryKeyId;

    @ApiModelProperty("信息资源编码")
    private String resourceCode;

    @ApiModelProperty("数据库类型（1-Oracle; 2-MySQL; 3-PostgreSQL）")
    private Long databaseType;

    @ApiModelProperty("数据源ID（关联汇聚平台）")
    private String datasourceId;

    @ApiModelProperty("表名（code）")
    private String tableSource;
}
