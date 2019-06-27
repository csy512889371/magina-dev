package com.rjsoft.magina.component.id.uid.config;

import com.rjsoft.magina.component.id.uid.impl.CachedUidGenerator;
import com.rjsoft.magina.component.id.uid.worker.service.WorkerIdAssignerService;
import com.rjsoft.magina.component.id.uid.worker.service.impl.DisposableWorkerIdAssignerServiceImpl;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class CachedUidGeneratorConfig {

    @Bean
    public WorkerIdAssignerService getWorkerIdAssigner() {
        return new DisposableWorkerIdAssignerServiceImpl();
    }

    @Bean
    public CachedUidGenerator getUidGenerator(WorkerIdAssignerService workerIdAssignerService) {

        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(workerIdAssignerService);
        /**
         * 一年 365*24*60*60=31536000秒。
         * 1、2的33次方秒 == 272年。 id生成器支持272年
         * 2、131072 万个 workid
         * 3、每秒中8192个序列
         */
        cachedUidGenerator.setTimeBits(33);
        cachedUidGenerator.setWorkerBits(17);
        cachedUidGenerator.setSeqBits(13);
        cachedUidGenerator.setEpochStr("2019-04-01");
        return cachedUidGenerator;
    }

}
