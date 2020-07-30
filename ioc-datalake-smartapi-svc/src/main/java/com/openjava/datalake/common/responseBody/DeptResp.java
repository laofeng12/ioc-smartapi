package com.openjava.datalake.common.responseBody;

import org.ljdp.component.result.BasicApiResponse;

import java.util.List;

public class DeptResp extends BasicApiResponse {

    private List<DeptInfo> rows;

    public DeptResp() {
    }

    public List<DeptInfo> getRows() {
        return this.rows;
    }

    public void setRows(List<DeptInfo> rows) {
        this.rows = rows;
    }

}