package com.openjava.datalake.org.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author JiaHai
 * @Description 组织机构资源 输出对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DlOrganizationResourceOutputDTO {
    private static final long serialVersionUID = -883087316657120196L;

    @ApiModelProperty("组织ID")
    private String orgId;

    @ApiModelProperty("组织名称")
    private String orgName;

    @ApiModelProperty("上级组织")
    private String parentId;

    @ApiModelProperty("资源目录数量（全市共享 + 局内私有）（上线+停更）")
    private Long resourceAmount;

    @ApiModelProperty("是否叶子结点")
    private Boolean leaf;

    @ApiModelProperty("子机构")
    private List<DlOrganizationResourceOutputDTO> children;
}
