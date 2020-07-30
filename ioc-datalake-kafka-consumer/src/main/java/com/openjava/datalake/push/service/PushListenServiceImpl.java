package com.openjava.datalake.push.service;

import com.openjava.datalake.push.dao.DlPushListenLogMapper;
import com.openjava.datalake.push.domain.DlPushListenLog;
import com.openjava.datalake.push.service.DlApiPushListenService;
import com.openjava.datalake.push.service.DlPushListenLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PushListenServiceImpl implements PushListenService {
    @Resource
    private DlApiPushListenService dlApiPushListenService;
    @Resource
    private DlPushListenLogMapper dlPushListenLogMapper;
    @Resource
    private DlPushListenLogService dlPushListenLogService;
    @Override
    public void pushLostenJob() {
        List<DlPushListenLog> nobegins = dlPushListenLogService.getNobegin();
        for (DlPushListenLog listenLog:nobegins
             ) {
            listenLog.setStates(0L);
            dlPushListenLogService.doSave(listenLog);
        }
        System.out.println(nobegins.size());
    }
}
