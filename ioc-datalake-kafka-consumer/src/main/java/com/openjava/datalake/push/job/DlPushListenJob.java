package com.openjava.datalake.push.job;

import com.openjava.datalake.push.service.PushListenService;
import com.openjava.datalake.push.service.DlPushListenLogService;
import org.ljdp.component.exception.APIException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DlPushListenJob {
    @Resource
    private PushListenService pushListenService;
    @Resource
    private DlPushListenLogService dlPushListenLogService;
    /**
     * 10秒扫描一次等待推送的数据
     *
     * @throws APIException
     */
    @Scheduled(cron = "${schedule.push-lister}")
    public void pushLostenJob() throws APIException {
//        System.out.println("定时任务：监听推送 开始执行");
        try {
            dlPushListenLogService.pushLostenJob();
        }catch (Exception e){
            System.out.println("定时任务：监听推送失败"+e.getMessage());
        }

//        System.out.println("定时任务：监听推送 结束执行");
    }
}
