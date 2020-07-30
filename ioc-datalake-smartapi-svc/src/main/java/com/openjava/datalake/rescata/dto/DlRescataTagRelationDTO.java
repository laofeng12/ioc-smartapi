package com.openjava.datalake.rescata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DlRescataTagRelationDTO implements Serializable {

    @ApiModelProperty("标签标识")
    private String  tagIden;

    @ApiModelProperty("标签名称")
    private String tagName;

    public DlRescataTagRelationDTO(String tagIden, String tagName) {
        this.tagIden = tagIden;
        this.tagName = tagName;
    }
}
