package com.multicampus.gamesungcoding.a11ymarketserver.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.s3")
data class S3StorageProperties(
    val bucket: String,
    val cdnUrl: String
) {
    init {
        require(bucket.isNotBlank()) { "S3 버킷 이름은 비어 있을 수 없습니다." }
        require(cdnUrl.startsWith("http")) { "CDN URL은 http 또는 https로 시작해야 합니다." }
    }
}
