package com.openjava.datalake.common.gateway;

import com.openjava.datalake.common.responseBody.SpDlResJwt;
import lombok.Data;

@Data
public class SpDlResJwtResp extends BaseResp {
    private SpDlResJwt data;

}
