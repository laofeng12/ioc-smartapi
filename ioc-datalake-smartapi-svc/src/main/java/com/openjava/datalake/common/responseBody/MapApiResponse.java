package com.openjava.datalake.common.responseBody;

import org.ljdp.component.result.ApiResponse;

import java.util.HashMap;

public class MapApiResponse implements ApiResponse {
    private String requestId;
    private Integer code;
    private String message;
    private String tub;
    private HashMap<String, Object> data;

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getTub() {
        return tub;
    }

    @Override
    public void setTub(String tub) {
        this.tub = tub;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }
}
