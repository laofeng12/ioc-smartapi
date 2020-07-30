package com.openjava.datalake.subscribe.service;

import com.openjava.datalake.subscribe.domain.DlSubscribeUnstrucPermi;
import com.openjava.datalake.subscribe.repository.DlSubscribeUnstrucPermiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author xjd
 * @Date 2019/12/16 20:45
 * @Version 1.0
 */
@Transactional
@Service
public class DlSubscribeUnstrucPermiServiceImpl implements DlSubscribeUnstrucPermiService {

    @Resource
    private DlSubscribeUnstrucPermiRepository dlSubscribeUnstrucPermiRepository;

    @Override
    public Map<Long, DlSubscribeUnstrucPermi> findAlreadyExistPermittedUnstructrue(String resourceCode, String userAccount) {
        List<DlSubscribeUnstrucPermi> dlSubscribeUnstrucPermis = dlSubscribeUnstrucPermiRepository.findByResourceCodeAndOwnerAccount(resourceCode, userAccount);
        Map<Long, DlSubscribeUnstrucPermi> dlSubscribeUnstrucPermiMap = dlSubscribeUnstrucPermis.stream().collect(Collectors.toMap(DlSubscribeUnstrucPermi::getUnstructureId, Function.identity()));
        return dlSubscribeUnstrucPermiMap;
    }
}
