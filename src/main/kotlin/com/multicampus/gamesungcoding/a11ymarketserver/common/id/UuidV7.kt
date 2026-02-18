package com.multicampus.gamesungcoding.a11ymarketserver.common.id

import org.hibernate.annotations.IdGeneratorType

@IdGeneratorType(UuidV7Generator::class)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
annotation class UuidV7 
