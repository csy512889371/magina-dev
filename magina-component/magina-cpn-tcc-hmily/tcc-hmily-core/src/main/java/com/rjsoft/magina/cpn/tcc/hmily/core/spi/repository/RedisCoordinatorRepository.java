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

package com.rjsoft.magina.cpn.tcc.hmily.core.spi.repository;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.rjsoft.magina.cpn.tcc.hmily.annotation.HmilySPI;
import com.rjsoft.magina.cpn.tcc.hmily.common.bean.adaper.CoordinatorRepositoryAdapter;
import com.rjsoft.magina.cpn.tcc.hmily.common.bean.entity.HmilyTransaction;
import com.rjsoft.magina.cpn.tcc.hmily.common.config.HmilyConfig;
import com.rjsoft.magina.cpn.tcc.hmily.common.config.HmilyRedisConfig;
import com.rjsoft.magina.cpn.tcc.hmily.common.enums.RepositorySupportEnum;
import com.rjsoft.magina.cpn.tcc.hmily.common.exception.HmilyException;
import com.rjsoft.magina.cpn.tcc.hmily.common.exception.HmilyRuntimeException;
import com.rjsoft.magina.cpn.tcc.hmily.common.jedis.JedisClient;
import com.rjsoft.magina.cpn.tcc.hmily.common.jedis.JedisClientCluster;
import com.rjsoft.magina.cpn.tcc.hmily.common.jedis.JedisClientSentinel;
import com.rjsoft.magina.cpn.tcc.hmily.common.jedis.JedisClientSingle;
import com.rjsoft.magina.cpn.tcc.hmily.common.serializer.ObjectSerializer;
import com.rjsoft.magina.cpn.tcc.hmily.common.utils.LogUtil;
import com.rjsoft.magina.cpn.tcc.hmily.common.utils.RepositoryConvertUtils;
import com.rjsoft.magina.cpn.tcc.hmily.common.utils.RepositoryPathUtils;
import com.rjsoft.magina.cpn.tcc.hmily.common.utils.StringUtils;
import com.rjsoft.magina.cpn.tcc.hmily.core.spi.HmilyCoordinatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * redis impl.
 *
 * @author xiaoyu
 */
@HmilySPI("redis")
public class RedisCoordinatorRepository implements HmilyCoordinatorRepository {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCoordinatorRepository.class);

    private ObjectSerializer objectSerializer;

    private JedisClient jedisClient;

    private String keyPrefix;

    @Override
    public int create(final HmilyTransaction hmilyTransaction) {
        try {
            final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, hmilyTransaction.getTransId());
            jedisClient.set(redisKey, RepositoryConvertUtils.convert(hmilyTransaction, objectSerializer));
            return ROWS;
        } catch (Exception e) {
            throw new HmilyRuntimeException(e);
        }
    }

    @Override
    public int remove(final String id) {
        try {
            final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, id);
            return jedisClient.del(redisKey).intValue();
        } catch (Exception e) {
            throw new HmilyRuntimeException(e);
        }
    }

    @Override
    public int update(final HmilyTransaction hmilyTransaction) throws HmilyRuntimeException {
        try {
            final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, hmilyTransaction.getTransId());
            hmilyTransaction.setVersion(hmilyTransaction.getVersion() + 1);
            hmilyTransaction.setLastTime(new Date());
            hmilyTransaction.setRetriedCount(hmilyTransaction.getRetriedCount());
            jedisClient.set(redisKey, RepositoryConvertUtils.convert(hmilyTransaction, objectSerializer));
            return ROWS;
        } catch (Exception e) {
            throw new HmilyRuntimeException(e);
        }
    }

    @Override
    public int updateParticipant(final HmilyTransaction hmilyTransaction) {
        try {
            final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, hmilyTransaction.getTransId());
            byte[] contents = jedisClient.get(redisKey.getBytes());
            CoordinatorRepositoryAdapter adapter = objectSerializer.deSerialize(contents, CoordinatorRepositoryAdapter.class);
            adapter.setContents(objectSerializer.serialize(hmilyTransaction.getHmilyParticipants()));
            jedisClient.set(redisKey, objectSerializer.serialize(adapter));
        } catch (HmilyException e) {
            e.printStackTrace();
            return FAIL_ROWS;
        }
        return ROWS;
    }

    @Override
    public int updateStatus(final String id, final Integer status) {
        try {
            final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, id);
            byte[] contents = jedisClient.get(redisKey.getBytes());
            if (contents != null) {
                CoordinatorRepositoryAdapter adapter = objectSerializer.deSerialize(contents, CoordinatorRepositoryAdapter.class);
                adapter.setStatus(status);
                jedisClient.set(redisKey, objectSerializer.serialize(adapter));
            }
        } catch (HmilyException e) {
            e.printStackTrace();
            return FAIL_ROWS;
        }
        return ROWS;
    }

    @Override
    public HmilyTransaction findById(final String id) {
        try {
            final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, id);
            byte[] contents = jedisClient.get(redisKey.getBytes());
            return RepositoryConvertUtils.transformBean(contents, objectSerializer);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<HmilyTransaction> listAll() {
        try {
            List<HmilyTransaction> transactions = Lists.newArrayList();
            Set<byte[]> keys = jedisClient.keys((keyPrefix + "*").getBytes());
            for (final byte[] key : keys) {
                byte[] contents = jedisClient.get(key);
                if (contents != null) {
                    transactions.add(RepositoryConvertUtils.transformBean(contents, objectSerializer));
                }
            }
            return transactions;
        } catch (Exception e) {
            throw new HmilyRuntimeException(e);
        }
    }

    @Override
    public List<HmilyTransaction> listAllByDelay(final Date date) {
        final List<HmilyTransaction> hmilyTransactions = listAll();
        return hmilyTransactions.stream()
                .filter(tccTransaction -> tccTransaction.getLastTime().compareTo(date) < 0)
                .collect(Collectors.toList());
    }

    @Override
    public void init(final String modelName, final HmilyConfig hmilyConfig) {
        keyPrefix = RepositoryPathUtils.buildRedisKeyPrefix(modelName);
        final HmilyRedisConfig hmilyRedisConfig = hmilyConfig.getHmilyRedisConfig();
        try {
            buildJedisPool(hmilyRedisConfig);
        } catch (Exception e) {
            LogUtil.error(LOGGER, "redis init error please check you config:{}", e::getMessage);
            throw new HmilyRuntimeException(e);
        }
    }

    @Override
    public String getScheme() {
        return RepositorySupportEnum.REDIS.getSupport();
    }

    @Override
    public void setSerializer(final ObjectSerializer objectSerializer) {
        this.objectSerializer = objectSerializer;
    }

    private void buildJedisPool(final HmilyRedisConfig hmilyRedisConfig) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(hmilyRedisConfig.getMaxIdle());
        config.setMinIdle(hmilyRedisConfig.getMinIdle());
        config.setMaxTotal(hmilyRedisConfig.getMaxTotal());
        config.setMaxWaitMillis(hmilyRedisConfig.getMaxWaitMillis());
        config.setTestOnBorrow(hmilyRedisConfig.getTestOnBorrow());
        config.setTestOnReturn(hmilyRedisConfig.getTestOnReturn());
        config.setTestWhileIdle(hmilyRedisConfig.getTestWhileIdle());
        config.setMinEvictableIdleTimeMillis(hmilyRedisConfig.getMinEvictableIdleTimeMillis());
        config.setSoftMinEvictableIdleTimeMillis(hmilyRedisConfig.getSoftMinEvictableIdleTimeMillis());
        config.setTimeBetweenEvictionRunsMillis(hmilyRedisConfig.getTimeBetweenEvictionRunsMillis());
        config.setNumTestsPerEvictionRun(hmilyRedisConfig.getNumTestsPerEvictionRun());
        JedisPool jedisPool;
        if (hmilyRedisConfig.getCluster()) {
            LogUtil.info(LOGGER, () -> "build redis cluster ............");
            final String clusterUrl = hmilyRedisConfig.getClusterUrl();
            final Set<HostAndPort> hostAndPorts =
                    Splitter.on(";")
                            .splitToList(clusterUrl)
                            .stream()
                            .map(HostAndPort::parseString).collect(Collectors.toSet());
            JedisCluster jedisCluster = new JedisCluster(hostAndPorts, config);
            jedisClient = new JedisClientCluster(jedisCluster);
        } else if (hmilyRedisConfig.getSentinel()) {
            LogUtil.info(LOGGER, () -> "build redis sentinel ............");
            final String sentinelUrl = hmilyRedisConfig.getSentinelUrl();
            final Set<String> hostAndPorts =
                    new HashSet<>(Splitter.on(";")
                            .splitToList(sentinelUrl));

            JedisSentinelPool pool =
                    new JedisSentinelPool(hmilyRedisConfig.getMasterName(), hostAndPorts,
                            config, hmilyRedisConfig.getTimeOut(), hmilyRedisConfig.getPassword());
            jedisClient = new JedisClientSentinel(pool);
        } else {
            if (StringUtils.isNoneBlank(hmilyRedisConfig.getPassword())) {
                jedisPool = new JedisPool(config, hmilyRedisConfig.getHostName(), hmilyRedisConfig.getPort(), hmilyRedisConfig.getTimeOut(), hmilyRedisConfig.getPassword());
            } else {
                jedisPool = new JedisPool(config, hmilyRedisConfig.getHostName(), hmilyRedisConfig.getPort(), hmilyRedisConfig.getTimeOut());
            }
            jedisClient = new JedisClientSingle(jedisPool);
        }
    }

}
