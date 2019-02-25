package com.rjsoft.magina.component.query.jpa.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class SnowflakeGenerator implements IdentifierGenerator {
    public static final String TYPE = "com.rjsoft.magina.component.query.jpa.util.SnowflakeGenerator";

    private static final IdWorker idWorker = new IdWorker();

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return idWorker.getId();
    }
}
