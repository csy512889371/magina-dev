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

package com.rjsoft.magina.cpn.tcc.hmily.core.service.impl;

import com.rjsoft.magina.cpn.tcc.hmily.common.bean.context.HmilyTransactionContext;
import com.rjsoft.magina.cpn.tcc.hmily.core.service.HmilyTransactionFactoryService;
import com.rjsoft.magina.cpn.tcc.hmily.core.service.handler.LocalHmilyTransactionHandler;
import com.rjsoft.magina.cpn.tcc.hmily.core.service.handler.ParticipantHmilyTransactionHandler;
import com.rjsoft.magina.cpn.tcc.hmily.core.service.handler.StarterHmilyTransactionHandler;
import com.rjsoft.magina.cpn.tcc.hmily.common.enums.HmilyRoleEnum;
import com.rjsoft.magina.cpn.tcc.hmily.core.service.handler.ConsumeHmilyTransactionHandler;
import org.springframework.stereotype.Service;

import java.util.Objects;

;

/**
 * HmilyTransactionFactoryServiceImpl.
 *
 * @author xiaoyu
 */
@Service("hmilyTransactionFactoryService")
public class HmilyTransactionFactoryServiceImpl implements HmilyTransactionFactoryService {

    /**
     * acquired HmilyTransactionHandler.
     *
     * @param context {@linkplain HmilyTransactionContext}
     * @return Class
     */
    @Override
    public Class factoryOf(final HmilyTransactionContext context) {
        if (Objects.isNull(context)) {
            return StarterHmilyTransactionHandler.class;
        } else {
            //why this code?  because spring cloud invoke has proxy.
            if (context.getRole() == HmilyRoleEnum.SPRING_CLOUD.getCode()) {
                context.setRole(HmilyRoleEnum.START.getCode());
                return ConsumeHmilyTransactionHandler.class;
            }
            // if context not null and role is inline  is ParticipantHmilyTransactionHandler.
            if (context.getRole() == HmilyRoleEnum.LOCAL.getCode()) {
                return LocalHmilyTransactionHandler.class;
            } else if (context.getRole() == HmilyRoleEnum.START.getCode()
                    || context.getRole() == HmilyRoleEnum.INLINE.getCode()) {
                return ParticipantHmilyTransactionHandler.class;
            }
            return ConsumeHmilyTransactionHandler.class;
        }
    }
}
