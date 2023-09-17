package com.itavgur.otushighload.config

import com.itavgur.otushighload.dao.*
import org.springframework.beans.factory.annotation.Autowired
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
@EnableConfigurationProperties(MysqlDBProperties::class)
@ConditionalOnProperty("db.type", havingValue = "mysql", matchIfMissing = false)
class MysqlDBConfig(
    @Autowired val props: MysqlDBProperties,
    @Lazy @Autowired val dataSource: DataSource,
) {

    val jdbcTemplate: JdbcTemplate = jdbcTemplate()
    val namedParameterJdbcTemplate: NamedParameterJdbcTemplate = namedParameterJdbcTemplate()

    @Bean
    fun userDao(): UserDao {
        return UserDaoMySql(namedParameterJdbcTemplate, CityDaoMySql(namedParameterJdbcTemplate))
    }

    @Bean
    fun credentialDao(): CredentialDao {
        return CredentialDaoMysql(namedParameterJdbcTemplate)
    }

    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .driverClassName(props.driverClassName)
            .url(props.url)
            .username(props.username)
            .password(props.password)
            .build()
    }

    final fun jdbcTemplate(): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    final fun namedParameterJdbcTemplate(): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }

}