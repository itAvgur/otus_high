package com.itavgur.otushighload.config

import com.itavgur.otushighload.dao.*
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
    fun credentialDao(): CredentialDao {
        return CredentialDaoMock()
    }

}