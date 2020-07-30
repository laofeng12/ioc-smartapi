package com.openjava.datalake.subscribe.service;

import com.openjava.datalake.subscribe.domain.DlSubscribeUnstrucPermi;
import java.util.Map;

/**
 * @Author xjd
 * @Date 2019/12/16 20:44
 * @Version 1.0
 */
public interface DlSubscribeUnstrucPermiService {
    Map<Long, DlSubscribeUnstrucPermi> findAlreadyExistPermittedUnstructrue(String resourceCode, String userAccount);

}
