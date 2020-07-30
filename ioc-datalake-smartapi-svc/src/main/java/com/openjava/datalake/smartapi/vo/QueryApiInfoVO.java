package com.openjava.datalake.smartapi.vo;

import com.openjava.datalake.smartapi.domain.DlApiQuery;
import com.openjava.datalake.smartapi.domain.DlApiRequest;
import com.openjava.datalake.smartapi.domain.DlApiResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 查询API所有信息，缓存到redis
 */
@Data
public class QueryApiInfoVO implements Serializable {
    public static final String KEY_API_CODE = "APICODE_";

    private DlApiQuery apiQuery;
    private List<DlApiRequest> apiRequests;
    private List<DlApiResponse> apiResponse;
    private ResourceTableVO resourceTableVO;
}
