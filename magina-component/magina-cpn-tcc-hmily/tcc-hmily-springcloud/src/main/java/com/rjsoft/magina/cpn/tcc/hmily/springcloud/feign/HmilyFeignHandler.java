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

package com.rjsoft.magina.cpn.tcc.hmily.springcloud.feign;


import com.rjsoft.magina.cpn.tcc.hmily.annotation.Hmily;
import com.rjsoft.magina.cpn.tcc.hmily.common.bean.entity.HmilyInvocation;
import com.rjsoft.magina.cpn.tcc.hmily.common.bean.entity.HmilyParticipant;
import com.rjsoft.magina.cpn.tcc.hmily.common.enums.HmilyActionEnum;
import com.rjsoft.magina.cpn.tcc.hmily.common.utils.StringUtils;
import com.rjsoft.magina.cpn.tcc.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import com.rjsoft.magina.cpn.tcc.hmily.core.helper.SpringBeanUtils;
import com.rjsoft.magina.cpn.tcc.hmily.common.bean.context.HmilyTransactionContext;
import com.rjsoft.magina.cpn.tcc.hmily.common.enums.HmilyRoleEnum;
import com.rjsoft.magina.cpn.tcc.hmily.core.service.executor.HmilyTransactionExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * HmilyFeignHandler.
 *
 * @author xiaoyu
 */
public class HmilyFeignHandler implements InvocationHandler {

    private InvocationHandler delegate;

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            final Hmily hmily = method.getAnnotation(Hmily.class);
            if (Objects.isNull(hmily)) {
                return this.delegate.invoke(proxy, method, args);
            }
            try {
                final HmilyTransactionContext hmilyTransactionContext = HmilyTransactionContextLocal.getInstance().get();
                if (Objects.nonNull(hmilyTransactionContext)) {
                    if (hmilyTransactionContext.getRole() == HmilyRoleEnum.LOCAL.getCode()) {
                        hmilyTransactionContext.setRole(HmilyRoleEnum.INLINE.getCode());
                    }
                }
                final HmilyTransactionExecutor hmilyTransactionExecutor =
                        SpringBeanUtils.getInstance().getBean(HmilyTransactionExecutor.class);
                final Object invoke = delegate.invoke(proxy, method, args);
                final HmilyParticipant hmilyParticipant = buildParticipant(hmily, method, args, hmilyTransactionContext);
                if (hmilyTransactionContext.getRole() == HmilyRoleEnum.INLINE.getCode()) {
                    hmilyTransactionExecutor.registerByNested(hmilyTransactionContext.getTransId(),
                            hmilyParticipant);
                } else {
                    hmilyTransactionExecutor.enlistParticipant(hmilyParticipant);
                }
                return invoke;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw throwable;
            }
        }
    }

    private HmilyParticipant buildParticipant(final Hmily hmily, final Method method, final Object[] args,
                                              final HmilyTransactionContext hmilyTransactionContext) {
        if (Objects.isNull(hmilyTransactionContext)
                || (HmilyActionEnum.TRYING.getCode() != hmilyTransactionContext.getAction())) {
            return null;
        }
        String confirmMethodName = hmily.confirmMethod();
        if (StringUtils.isBlank(confirmMethodName)) {
            confirmMethodName = method.getName();
        }
        String cancelMethodName = hmily.cancelMethod();
        if (StringUtils.isBlank(cancelMethodName)) {
            cancelMethodName = method.getName();
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        HmilyInvocation confirmInvocation = new HmilyInvocation(declaringClass, confirmMethodName, method.getParameterTypes(), args);
        HmilyInvocation cancelInvocation = new HmilyInvocation(declaringClass, cancelMethodName, method.getParameterTypes(), args);
        return new HmilyParticipant(hmilyTransactionContext.getTransId(), confirmInvocation, cancelInvocation);
    }

    void setDelegate(InvocationHandler delegate) {
        this.delegate = delegate;
    }

}
