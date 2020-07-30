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
 * @Description 数据库连接参数信息
 */
@Entity
@Table(name = "DL_RESCATA_DATABASE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DlRescataDatabase implements Serializable {
    private static final long serialVersionUID = -8074473229232978374L;

    @Id
    @ApiModelProperty("数据库编码")
    private Long databaseId;
    @ApiModelProperty("数据库名称")
    private String databaseName;
    @ApiModelProperty("数据库连接JSON信息")
    private String databaseJsonInfo;
    @ApiModelProperty("分库类别（dl.resource.repository.type）（1归集库、2中心库、3基础库、4主题库、5专题库）")
    private Long repositoryType;
    @ApiModelProperty("数据库类型（1HIVE、2MPP）")
    private Long databaseType;

    @ApiModelProperty("是否用于数据挂载（1是、2否）")
    private Long isUseForMount;



}
