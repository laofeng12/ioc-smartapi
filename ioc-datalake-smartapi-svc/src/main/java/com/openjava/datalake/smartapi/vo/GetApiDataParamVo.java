package com.openjava.datalake.smartapi.vo;

import com.openjava.datalake.subscribe.domain.DlDataAccessLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author xjd
 * @Date 2020/7/14 10:22
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetApiDataParamVo {
    private String version;
    @NotNull
    private DlDataAccessLog dlDataAccessLog;
    @NotNull
    private Boolean passDatalakeAuth;
}
