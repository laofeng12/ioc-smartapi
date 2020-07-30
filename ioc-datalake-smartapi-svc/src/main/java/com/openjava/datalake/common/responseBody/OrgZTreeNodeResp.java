package com.openjava.datalake.common.responseBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xjd
 * @Date 2019/11/15 15:40
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgZTreeNodeResp {

    private String id;
    private String name;
    private String pId;
    private Boolean open;
    private Boolean drag;
    private Integer sort;
    private String refId;
    private Boolean checked;
    private Boolean nocheck;

    // 这里头字母要写成小写才能自动注入
    public String getpId() {
        return pId;
    }
    // 这里头字母要写成小写才能自动注入
    public void setpId(String pId) {
        this.pId = pId;
    }
}
