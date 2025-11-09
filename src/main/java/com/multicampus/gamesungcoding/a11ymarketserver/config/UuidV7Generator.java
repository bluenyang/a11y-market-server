package com.multicampus.gamesungcoding.a11ymarketserver.config;

import com.github.f4b6a3.uuid.alt.GUID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;


public class UuidV7Generator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner) throws HibernateException {
        return GUID.v7().toUUID();
    }
}
