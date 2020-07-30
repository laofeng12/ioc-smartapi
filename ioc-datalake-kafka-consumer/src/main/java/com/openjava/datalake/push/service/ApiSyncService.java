package com.openjava.datalake.push.service;

import org.ljdp.component.exception.APIException;

import java.sql.Connection;

/**
 * @Author xjd
 * @Date 2019/8/29 9:39
 * @Version 1.0
 */
public interface ApiSyncService {

    Connection getConnThroughHikarByResId(Long resourceId) throws APIException;
}
