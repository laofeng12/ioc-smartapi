package com.openjava.datalake.rescata.param;

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
 * @Description 资源目录非结构化文件关系表
 */
@Entity
@Table(name = "DL_RESOURCE_UNSTRUCTURED_FILE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DlResourceUnstructuredFile implements Serializable {
    @Id
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("资源目录ID")
    private Long resourceId;

    @ApiModelProperty("信息资源编码")
    private String resourceCode;

    @ApiModelProperty("文件对象唯一标识")
    private String objectKey;
}
