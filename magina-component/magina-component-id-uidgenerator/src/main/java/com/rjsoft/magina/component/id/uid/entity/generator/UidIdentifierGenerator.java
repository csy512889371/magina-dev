package com.rjsoft.magina.component.id.uid.entity.generator;

import com.rjsoft.magina.component.id.uid.UidGenerator;
import com.rjsoft.magina.component.id.uid.entity.utils.UidApplicationContextHolder;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;


public class UidIdentifierGenerator implements IdentifierGenerator {
    public static final String TYPE = "com.rjsoft.magina.component.id.uid.entity.generator.UidIdentifierGenerator";

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {

        UidGenerator uidGenerator = UidApplicationContextHolder.getBean(UidGenerator.class);
        return String.valueOf(uidGenerator.getUID());
    }
}
