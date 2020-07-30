package com.openjava.datalake.common.responseBody;

import java.util.ArrayList;
import java.util.List;

public class OrgVO {
    private String orgId;//组织id
    private String orgName;//组织名称
    private String parentId;//上级组织id
    private String orgtype;//组织类型

    private List<OrgVO> subOrgList;//下级组织列表

    public OrgVO() {
    }

    public OrgVO(String orgId, String orgName, String parentId, String orgtype) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.parentId = parentId;
        this.orgtype = orgtype;
    }

    public String getOrgId() {
        return this.orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getParentId() {
        return this.parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrgtype() {
        return this.orgtype;
    }
    public void setOrgtype(String orgtype) {
        this.orgtype = orgtype;
    }

    public List<OrgVO> getSubOrgList() {
        return this.subOrgList;
    }
    public void setSubOrgList(List<OrgVO> subOrgList) {
        this.subOrgList = subOrgList;
    }

    public void addSubResource(OrgVO res) {
        if (this.subOrgList == null) {
            this.subOrgList = new ArrayList();
        }

        this.subOrgList.add(res);
    }
}
