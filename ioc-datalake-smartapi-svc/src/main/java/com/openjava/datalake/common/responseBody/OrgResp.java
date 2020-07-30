package com.openjava.datalake.common.responseBody;

import org.ljdp.component.result.BasicApiResponse;

import java.util.List;

public class OrgResp extends BasicApiResponse {
    private List<OrgVO> resources;

    public OrgResp() {
    }

    public List<OrgVO> getResources() {
        return this.resources;
    }

    public void setResources(List<OrgVO> resources) {
        this.resources = resources;
    }

}
