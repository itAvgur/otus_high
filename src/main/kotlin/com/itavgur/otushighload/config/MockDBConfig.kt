package com.itavgur.otushighload.config

import com.itavgur.otushighload.dao.CredentialDao
import com.itavgur.otushighload.dao.CredentialDaoMock
import com.itavgur.otushighload.dao.UserDao
import com.itavgur.otushighload.dao.UserDaoMock
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
    fun credentialDao(): CredentialDao {
        return CredentialDaoMock()
    }

}