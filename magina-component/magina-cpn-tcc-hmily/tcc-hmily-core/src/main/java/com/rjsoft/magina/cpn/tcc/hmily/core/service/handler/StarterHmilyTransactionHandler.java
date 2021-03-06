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

package com.rjsoft.magina.cpn.tcc.hmily.core.service.handler;

import com.rjsoft.magina.cpn.tcc.hmily.core.service.HmilyTransactionHandler;
import com.rjsoft.magina.cpn.tcc.hmily.common.bean.context.HmilyTransactionContext;
import com.rjsoft.magina.cpn.tcc.hmily.common.bean.entity.HmilyTransaction;
import com.rjsoft.magina.cpn.tcc.hmily.common.config.HmilyConfig;
import com.rjsoft.magina.cpn.tcc.hmily.common.enums.HmilyActionEnum;
import com.rjsoft.magina.cpn.tcc.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.DisruptorProviderManage;
import com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.handler.HmilyConsumerTransactionDataHandler;
import com.rjsoft.magina.cpn.tcc.hmily.core.service.HmilyTransactionHandlerAlbum;
import com.rjsoft.magina.cpn.tcc.hmily.core.service.executor.HmilyTransactionExecutor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * this is hmily transaction starter.
 *
 * @author xiaoyu
 */
@Component
public class StarterHmilyTransactionHandler implements HmilyTransactionHandler, ApplicationListener<ContextRefreshedEvent> {

    private final HmilyTransactionExecutor hmilyTransactionExecutor;

    private final HmilyConfig hmilyConfig;

    private DisruptorProviderManage<HmilyTransactionHandlerAlbum> disruptorProviderManage;

    /**
     * Instantiates a new Starter hmily transaction handler.
     *
     * @param hmilyTransactionExecutor the hmily transaction executor
     * @param hmilyConfig              the hmily config
     */
    @Autowired
    public StarterHmilyTransactionHandler(final HmilyTransactionExecutor hmilyTransactionExecutor, final HmilyConfig hmilyConfig) {
        this.hmilyTransactionExecutor = hmilyTransactionExecutor;
        this.hmilyConfig = hmilyConfig;
    }

    @Override
    public Object handler(final ProceedingJoinPoint point, final HmilyTransactionContext context)
            throws Throwable {
        Object returnValue;
        try {
            HmilyTransaction hmilyTransaction = hmilyTransactionExecutor.preTry(point);
            try {
                //execute try
                returnValue = point.proceed();
                hmilyTransaction.setStatus(HmilyActionEnum.TRYING.getCode());
                hmilyTransactionExecutor.updateStatus(hmilyTransaction);
            } catch (Throwable throwable) {
                //if exception ,execute cancel
                final HmilyTransaction currentTransaction = hmilyTransactionExecutor.getCurrentTransaction();
                disruptorProviderManage.getProvider().onData(() -> hmilyTransactionExecutor.cancel(currentTransaction));
                throw throwable;
            }
            //execute confirm
            final HmilyTransaction currentTransaction = hmilyTransactionExecutor.getCurrentTransaction();
            disruptorProviderManage.getProvider().onData(() -> hmilyTransactionExecutor.confirm(currentTransaction));
        } finally {
            HmilyTransactionContextLocal.getInstance().remove();
            hmilyTransactionExecutor.remove();
        }
        return returnValue;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (hmilyConfig.getStarted()) {
            disruptorProviderManage = new DisruptorProviderManage<>(new HmilyConsumerTransactionDataHandler(),
                    hmilyConfig.getAsyncThreads(),
                    DisruptorProviderManage.DEFAULT_SIZE);
            disruptorProviderManage.startup();
        }

    }
}
