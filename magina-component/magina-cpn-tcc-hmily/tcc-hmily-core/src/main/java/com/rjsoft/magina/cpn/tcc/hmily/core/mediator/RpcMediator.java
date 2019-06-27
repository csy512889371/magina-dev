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

package com.rjsoft.magina.cpn.tcc.hmily.core.mediator;


import com.rjsoft.magina.cpn.tcc.hmily.common.bean.context.HmilyTransactionContext;
import com.rjsoft.magina.cpn.tcc.hmily.common.constant.CommonConstant;
import com.rjsoft.magina.cpn.tcc.hmily.common.enums.HmilyRoleEnum;
import com.rjsoft.magina.cpn.tcc.hmily.common.utils.GsonUtils;
import com.rjsoft.magina.cpn.tcc.hmily.common.utils.StringUtils;

/**
 * The type RpcMediator.
 * 调停者
 *
 * @author xiaoyu(Myth)
 */
public class RpcMediator {

    private static final RpcMediator RPC_MEDIATOR = new RpcMediator();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static RpcMediator getInstance() {
        return RPC_MEDIATOR;
    }


    /**
     * Transmit.
     *
     * @param rpcTransmit the rpc mediator
     * @param context     the context
     */
    public void transmit(final RpcTransmit rpcTransmit, final HmilyTransactionContext context) {
        if (context != null) {
            if (context.getRole() == HmilyRoleEnum.LOCAL.getCode()) {
                context.setRole(HmilyRoleEnum.INLINE.getCode());
            }
            rpcTransmit.transmit(CommonConstant.HMILY_TRANSACTION_CONTEXT,
                    GsonUtils.getInstance().toJson(context));
        }
    }

    /**
     * Acquire hmily transaction context.
     *
     * @param rpcAcquire the rpc acquire
     * @return the hmily transaction context
     */
    public HmilyTransactionContext acquire(RpcAcquire rpcAcquire) {
        HmilyTransactionContext hmilyTransactionContext = null;
        final String context = rpcAcquire.acquire(CommonConstant.HMILY_TRANSACTION_CONTEXT);
        if (StringUtils.isNoneBlank(context)) {
            hmilyTransactionContext = GsonUtils.getInstance().fromJson(context, HmilyTransactionContext.class);
        }
        return hmilyTransactionContext;
    }
}
