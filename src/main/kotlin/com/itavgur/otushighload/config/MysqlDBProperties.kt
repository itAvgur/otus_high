package com.itavgur.otushighload.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConditionalOnProperty("db.type", havingValue = "mysql", matchIfMissing = false)
@ConfigurationProperties(prefix = "db.mysql")
class MysqlDBProperties(
    val driverClassName: String = "com.mysql.cj.jdbc.Driver",
    val url: String,
    val username: String,
    val password: String
)