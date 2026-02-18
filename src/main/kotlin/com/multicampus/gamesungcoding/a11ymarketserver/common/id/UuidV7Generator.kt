package com.multicampus.gamesungcoding.a11ymarketserver.common.id;

import com.github.f4b6a3.uuid.alt.GUID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.UUID;

public class UuidV7Generator implements IdentifierGenerator {
    @Override
    public UUID generate(SharedSessionContractImplementor session, Object owner) throws HibernateException {
        return GUID.v7().toUUID();
    }
}
