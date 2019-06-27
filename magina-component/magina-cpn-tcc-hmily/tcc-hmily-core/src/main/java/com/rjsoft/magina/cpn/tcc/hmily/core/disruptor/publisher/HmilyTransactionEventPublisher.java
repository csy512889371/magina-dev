/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.publisher;


import com.rjsoft.magina.cpn.tcc.hmily.common.enums.EventTypeEnum;
import com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.handler.HmilyConsumerLogDataHandler;
import com.rjsoft.magina.cpn.tcc.hmily.common.bean.entity.HmilyTransaction;
import com.rjsoft.magina.cpn.tcc.hmily.common.config.HmilyConfig;
import com.rjsoft.magina.cpn.tcc.hmily.core.concurrent.SingletonExecutor;
import com.rjsoft.magina.cpn.tcc.hmily.core.concurrent.ConsistentHashSelector;
import com.rjsoft.magina.cpn.tcc.hmily.core.coordinator.HmilyCoordinatorService;
import com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.DisruptorProviderManage;
import com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.event.HmilyTransactionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * event publisher.
 *
 * @author xiaoyu(Myth)
 */
@Component
public class HmilyTransactionEventPublisher implements ApplicationListener<ContextRefreshedEvent> {

    private DisruptorProviderManage<HmilyTransactionEvent> disruptorProviderManage;

    private final HmilyCoordinatorService coordinatorService;

    private final HmilyConfig hmilyConfig;

    @Autowired
    public HmilyTransactionEventPublisher(final HmilyCoordinatorService coordinatorService,
                                          final HmilyConfig hmilyConfig) {
        this.coordinatorService = coordinatorService;
        this.hmilyConfig = hmilyConfig;
    }

    /**
     * disruptor start.
     *
     * @param bufferSize this is disruptor buffer size.
     * @param threadSize this is disruptor consumer thread size.
     */
    private void start(final int bufferSize, final int threadSize) {
        List<SingletonExecutor> selects = new ArrayList<>();
        for (int i = 0; i < threadSize; i++) {
            selects.add(new SingletonExecutor("hmily-log-disruptor" + i));
        }
        ConsistentHashSelector selector = new ConsistentHashSelector(selects);
        disruptorProviderManage =
                new DisruptorProviderManage<>(
                        new HmilyConsumerLogDataHandler(selector, coordinatorService), 1, bufferSize);
        disruptorProviderManage.startup();
    }

    /**
     * publish disruptor event.
     *
     * @param hmilyTransaction {@linkplain HmilyTransaction }
     * @param type             {@linkplain EventTypeEnum}
     */
    public void publishEvent(final HmilyTransaction hmilyTransaction, final int type) {
        HmilyTransactionEvent event = new HmilyTransactionEvent();
        event.setType(type);
        event.setHmilyTransaction(hmilyTransaction);
        disruptorProviderManage.getProvider().onData(event);
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        start(hmilyConfig.getBufferSize(), hmilyConfig.getConsumerThreads());
    }
}
