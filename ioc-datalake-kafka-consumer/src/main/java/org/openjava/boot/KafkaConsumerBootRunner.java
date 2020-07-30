package org.openjava.boot;

import com.openjava.datalake.common.PublicConstant;
import org.ljdp.common.spring.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.Resource;

public class KafkaConsumerBootRunner implements ApplicationRunner {

	@Autowired
	private SpringContext springContext;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		String[] actives = springContext.getContext().getEnvironment().getActiveProfiles();
		PublicConstant.ACTIVE = actives[0];
		PublicConstant.initHuaweiEnv();
	}

}
