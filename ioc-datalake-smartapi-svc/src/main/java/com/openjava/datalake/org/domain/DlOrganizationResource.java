package com.openjava.datalake.org.domain;

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
 * @Description 组织机构资源表 实体类
 */
@Entity
@Table(name = "DL_ORGANIZATION_RESOURCE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DlOrganizationResource implements Serializable {
    private static final long serialVersionUID = 208637593032275676L;

    @Id
    @ApiModelProperty("组织ID")
    private String orgId;

    @ApiModelProperty("组织编码")
    private String deptCode;

    @ApiModelProperty("组织名称")
    private String orgName;

    @ApiModelProperty("上级组织")
    private String parentId;

    @ApiModelProperty("组织类型")
    private String orgType;

    @ApiModelProperty("路径")
    private String fullPath;

    @ApiModelProperty("资源目录数量（全市共享 + 局内私有）（上线+停更）")
    private Long resourceAmount;
}