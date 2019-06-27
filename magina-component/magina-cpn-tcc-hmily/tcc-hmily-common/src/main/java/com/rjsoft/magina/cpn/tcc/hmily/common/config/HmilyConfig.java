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

package com.rjsoft.magina.cpn.tcc.hmily.common.config;

import com.rjsoft.magina.cpn.tcc.hmily.common.enums.RepositorySupportEnum;
import com.rjsoft.magina.cpn.tcc.hmily.common.enums.SerializeEnum;
import lombok.Data;


/**
 * hmily config.
 *
 * @author xiaoyu
 */
@Data
public class HmilyConfig {

    /**
     * Resource suffix this parameter please fill in about is the transaction store path.
     * If it's a table store this is a table suffix, it's stored the same way.
     * If this parameter is not filled in, the applicationName of the application is retrieved by default
     */
    private String repositorySuffix;

    /**
     * this is map db concurrencyScale.
     */
    private Integer concurrencyScale = 512;

    /**
     * log serializer.
     * {@linkplain SerializeEnum}
     * 这里我推荐使用是kroy。当然hmily也支持hessian,protostuff,jdk。在我们测试中表现为
     */
    private String serializer = "kryo";

    /**
     * scheduledPool Thread size.
     */
    private int scheduledThreadMax = Runtime.getRuntime().availableProcessors() << 1;

    /**
     * scheduledPool scheduledDelay unit SECONDS.
     */
    private int scheduledDelay = 60;

    /**
     * scheduledPool scheduledInitDelay unit SECONDS.
     */
    private int scheduledInitDelay = 120;

    /**
     * retry max.
     * 最大重复次数，默认3次。当你的服务down机，定时任务会执行retryMax次数去执行你的cancel还是confrim。
     */
    private int retryMax = 3;

    /**
     * 定时任务延迟时间（单位是秒，默认120。这个参数只是要大于你的rpc调用的超时时间设置。
     * recoverDelayTime Unit seconds
     * (note that this time represents how many seconds after the local transaction was created before execution).
     */
    private int recoverDelayTime = 60;

    /**
     * Parameters when participants perform their own recovery.
     * 1.such as RPC calls time out
     * 2.such as the starter down machine
     */
    private int loadFactor = 2;

    /**
     * repositorySupport.
     * {@linkplain RepositorySupportEnum}
     */
    private String repositorySupport = "db";

    /**
     * disruptor bufferSize.
     * disruptor的bufferSize,当高并发的时候，可以调大。注意是 2n
     */
    private int bufferSize = 4096 * 2 * 2;

    /**
     * this is disruptor consumerThreads.
     * distuptor消费线程数量,高并发的时候，可以调大。
     */
    private int consumerThreads = Runtime.getRuntime().availableProcessors() << 1;

    /**
     * this is hmily async execute cancel or confirm thread size.
     * 异步执行confirm和cancel线程池线程的大小，高并发的时候请调大
     */
    private int asyncThreads = Runtime.getRuntime().availableProcessors() << 1;

    /**
     * when start this set true  actor set false.
     * 注意在是发起方的时候，把此属性设置为true。参与方为false
     */
    private Boolean started = true;

    /**
     * db config.
     */
    private HmilyDbConfig hmilyDbConfig;

    /**
     * mongo config.
     */
    private HmilyMongoConfig hmilyMongoConfig;

    /**
     * redis config.
     */
    private HmilyRedisConfig hmilyRedisConfig;

    /**
     * zookeeper config.
     */
    private HmilyZookeeperConfig hmilyZookeeperConfig;

    /**
     * file config.
     */
    private HmilyFileConfig hmilyFileConfig;

}
