package org.openjava.boot;

import com.openjava.datalake.common.PublicConstant;
import org.ljdp.common.cache.CacheParam;
import org.ljdp.common.cache.CacheType;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.spring.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class DataLakeSmartApiRunner implements ApplicationRunner {
    @Autowired
    private SpringContext springContext;


    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("初始化jvm缓冲区");
        //初始化一个JVM缓存区域，类型为：生命周期型
        MemoryCache.initCache(PublicConstant.GAT_WAY_USERINFO_DATA, CacheType.LIFE);
        //设置缓存默认时间为10分钟
        MemoryCache.config(PublicConstant.GAT_WAY_USERINFO_DATA, CacheParam.TIME_LIFE_MINUTE, 10);
//        System.out.println("初始化完成");

    }
}
