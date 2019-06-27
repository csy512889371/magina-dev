package com.rjsoft.magina.component.id.idleaf.entity.generator;

import com.rjsoft.magina.component.id.idleaf.entity.utils.IdleafApplicationContextHolder;
import com.rjsoft.magina.component.id.idleaf.service.IdLeafServiceFactory;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;


public class IdleafIdentifierGenerator implements IdentifierGenerator {
    public static final String TYPE = "com.rjsoft.magina.component.id.idleaf.entity.generator.IdleafIdentifierGenerator";

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {

        String name = o.getClass().getSimpleName();
        IdLeafServiceFactory idLeafServiceFactory = IdleafApplicationContextHolder.getBean(IdLeafServiceFactory.class);
        return idLeafServiceFactory.nextStringValue(name);
    }
}
