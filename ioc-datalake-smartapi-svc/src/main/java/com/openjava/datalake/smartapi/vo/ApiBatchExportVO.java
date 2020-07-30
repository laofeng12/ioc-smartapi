package com.openjava.datalake.smartapi.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class ApiBatchExportVO implements Serializable{

    @ApiModelProperty("API主键ID集合")
    private List<Long> ids;//api主键ids

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

}
