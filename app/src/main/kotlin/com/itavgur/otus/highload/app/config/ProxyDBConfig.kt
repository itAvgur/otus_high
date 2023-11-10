package com.itavgur.otus.highload.app.config

import com.itavgur.otus.highload.app.dao.*
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ProxyDBProperties::class)
@ConditionalOnProperty("db.type", havingValue = "proxy", matchIfMissing = false)
class ProxyDBConfig(
    val props: ProxyDBProperties
) {

    @Bean
    fun userDao(): UserDao {
        return UserDaoProxy(props)
    }

    @Bean
    fun postDao(): PostDao {
        return PostDaoMock()
    }

    @Bean
    fun friendDao(): FriendDao {
        return FriendDaoMock()
    }

    @Bean
    fun messageDao(): MessageDao {
        return MessageDaoMock()
    }

    @Bean
    fun credentialDao(): CredentialDao {
        return CredentialDaoMock()
    }

}