package com.itavgur.otushighload.service

import com.itavgur.otushighload.dao.CredentialDao
import com.itavgur.otushighload.domain.Credential
import com.itavgur.otushighload.exception.CredentialException
import com.itavgur.otushighload.web.dto.AuthenticationDtoRequest
import com.itavgur.otushighload.web.dto.AuthenticationDtoResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class CredentialService(
    @Autowired val credentialDao: CredentialDao,
    @Autowired val passwordEncoder: PasswordEncoder
) {

    @Value("\${token.expired.days: 0}")
    val tokenExpiredDays: Int = 0

    fun saveCredential(credential: Credential, userId: Int): Credential {

        credential.id?.let {
            credentialDao.getCredentialById(credential.id!!)
                ?.let {
                    credential.pass = encodePassword(credential.pass)
                    return credentialDao.updateCredential(credential)
                }
            throw CredentialException("user with id ${credential.id} missed")
        }

        credential.pass = encodePassword(credential.pass)
        return credentialDao.insertCredential(credential = credential, userId = userId)
    }

    fun checkCredential(credential: Credential): Boolean {

        credentialDao.getCredentialByLogin(credential.login)
            ?.let {
                return it.enabled && matchPassword(credential.pass, it.pass)
            }
        return false
    }

    fun authenticate(authenticationDto: AuthenticationDtoRequest): AuthenticationDtoResponse {

        val credential = credentialDao.getCredentialByLogin(authenticationDto.login)

        if (credential?.enabled == true
            && matchPassword(authenticationDto.pass, credential.pass)
        ) {
            credential.token = UUID.randomUUID().toString()
            credential.tokenExpired = LocalDate.now().plusDays(tokenExpiredDays.toLong())
            credentialDao.updateCredential(credential)
            return AuthenticationDtoResponse(token = credential.token!!, tokenExpired = credential.tokenExpired!!)
        }

        throw CredentialException("Invalid login/password", HttpStatus.UNAUTHORIZED)
    }

    @Throws(CredentialException::class)
    fun checkAuthentication(token: String?) {

        if (token == null) {
            throw CredentialException("User not authenticated", HttpStatus.UNAUTHORIZED)
        }

        val credential = credentialDao.getCredentialByToken(token)
        if (credential == null || LocalDate.now().isAfter(credential.tokenExpired)) {
            throw CredentialException("User not authenticated", HttpStatus.UNAUTHORIZED)
        }
    }

    fun destroyCredential(credential: Credential): Int {
        return credentialDao.destroyCredential(credential)
    }

    fun encodePassword(password: String): String {
        return passwordEncoder.encode(password)
    }

    fun matchPassword(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }

}