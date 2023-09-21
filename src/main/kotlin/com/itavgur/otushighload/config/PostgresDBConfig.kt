package com.itavgur.otushighload.config

import com.itavgur.otushighload.dao.*
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(PostgresDBProperties::class)
@ConditionalOnProperty("db.type", havingValue = "postgres", matchIfMissing = false)
class PostgresDBConfig(
    val props: PostgresDBProperties,
    @Lazy val dataSource: DataSource,
) {

    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .driverClassName(props.driverClassName)
            .url(props.url)
            .username(props.username)
            .password(props.password)
            .build()
    }

    @Bean
    fun jdbcTemplate(): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    @Bean
    fun namedParameterJdbcTemplate(): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }

}