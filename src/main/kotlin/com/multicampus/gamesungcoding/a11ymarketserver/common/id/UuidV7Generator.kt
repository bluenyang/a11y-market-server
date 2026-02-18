package com.multicampus.gamesungcoding.a11ymarketserver.common.id

import com.github.f4b6a3.uuid.alt.GUID
import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.util.*

class UuidV7Generator : IdentifierGenerator {
    override fun generate(session: SharedSessionContractImplementor, owner: Any?): UUID {
        return GUID.v7().toUUID()
    }
}
