package com.openjava.datalake.org.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author hqr
 * @Description 树结点对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DlOrganizationResourceTreeOutputDTO {

    @ApiModelProperty("结点ID")
    private String orgId;

    @ApiModelProperty("结点名称")
    private String orgName;

    @ApiModelProperty("上级组织")
    private String parentId;

    @ApiModelProperty("资源目录数量（全市共享 + 局内私有）（上线+停更）")
    private Long resourceAmount;

    @ApiModelProperty("是否叶子结点")
    private Boolean leaf;

    @ApiModelProperty("子机构")
    private List<DlOrganizationResourceTreeOutputDTO> children;

    @ApiModelProperty("结点类型 0.根,1.归属库,2.组织机构,3.资源目录类型")
    private Long nodeType;

    @ApiModelProperty("归属库类型 0.当前结点无归属库,2.标准库,3.基础库,4.主题库,5.专题库")
    private Long resourceRepositoryType;

}
