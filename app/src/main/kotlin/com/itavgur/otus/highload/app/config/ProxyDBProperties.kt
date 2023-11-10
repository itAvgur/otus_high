package com.itavgur.otus.highload.app.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties

@ConditionalOnBean(ProxyDBConfig::class)
@ConfigurationProperties(prefix = "db.proxy")
class ProxyDBProperties(
    val driverClassName: String = "org.postgresql.Driver",
    val url: String,
    val username: String,
    val password: String
)