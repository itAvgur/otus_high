package com.itavgur.otus.highload.app.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties

@ConditionalOnBean(MysqlDBConfig::class)
@ConfigurationProperties(prefix = "db.mysql")
class MysqlDBProperties(
    val driverClassName: String = "com.mysql.cj.jdbc.Driver",
    val url: String,
    val username: String,
    val password: String
)