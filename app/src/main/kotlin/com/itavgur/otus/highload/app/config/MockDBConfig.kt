package com.itavgur.otus.highload.app.config

import com.itavgur.otus.highload.app.dao.*
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty("db.type", havingValue = "mock", matchIfMissing = true)
class MockDBConfig {

    @Bean
    fun userDao(): UserDao {
        return UserDaoMock()
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