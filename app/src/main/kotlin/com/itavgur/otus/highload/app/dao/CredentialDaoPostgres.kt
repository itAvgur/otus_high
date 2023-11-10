package com.itavgur.otus.highload.app.dao

import com.itavgur.otus.highload.app.config.PostgresDBConfig
import com.itavgur.otus.highload.app.domain.Credential
import com.itavgur.otus.highload.app.util.DBUtil
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
@ConditionalOnBean(PostgresDBConfig::class)
class CredentialDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : CredentialDao {

    companion object {

        const val QUERY_CREDENTIAL_BY_ID =
            """SELECT cred.id ,cred.user_id, cred.login, cred.pass, cred.token, cred.token_expired, cred.enabled
                FROM credentials cred
                where cred.id = :credId"""

        const val QUERY_CREDENTIAL_BY_LOGIN =
            """SELECT cred.id ,cred.user_id, cred.login, cred.pass, cred.token, cred.token_expired, cred.enabled
                FROM credentials cred
                where cred.login = :login"""

        const val QUERY_CREDENTIAL_BY_TOKEN =
            """SELECT cred.id ,cred.user_id, cred.login, cred.pass, cred.token, cred.token_expired, cred.enabled
                FROM credentials cred
                where cred.token = :token"""

        const val QUERY_INSERT_CREDENTIAL =
            """INSERT INTO credentials (user_id, login, pass, token, token_expired, enabled)
                VALUES (:userId, :login, :pass, :token, :tokenExpired, :enabled)"""

        const val QUERY_UPDATE_CREDENTIAL =
            """UPDATE credentials
                SET login = :login, pass = :pass, token = :token, token_expired = :tokenExpired, enabled = :enabled
                WHERE credentials.id = :id"""
    }

    override fun insertCredential(credential: Credential, userId: Int): Credential {

        val map = MapSqlParameterSource(
            mapOf(
                "userId" to userId,
                "login" to credential.login,
                "pass" to credential.pass,
                "token" to credential.token,
                "tokenExpired" to credential.tokenExpired,
                "enabled" to credential.enabled
            )
        )

        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_CREDENTIAL, map, generatedKeyHolder)
        credential.id = generatedKeyHolder.keyList.first()["id"] as Int?

        return credential
    }

    override fun updateCredential(credential: Credential): Credential {
        val map = MapSqlParameterSource(
            mapOf(
                "id" to credential.id,
                "userId" to credential.userId,
                "login" to credential.login,
                "pass" to credential.pass,
                "token" to credential.token,
                "tokenExpired" to credential.tokenExpired,
                "enabled" to credential.enabled
            )
        )

        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_UPDATE_CREDENTIAL, map, generatedKeyHolder)

        return credential
    }

    override fun getCredentialById(id: Int): Credential? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_CREDENTIAL_BY_ID, MapSqlParameterSource("credId", id),
                CredentialRowMapper()
            )
        return result.firstOrNull()
    }

    override fun getCredentialByLogin(login: String): Credential? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_CREDENTIAL_BY_LOGIN, MapSqlParameterSource("login", login),
                CredentialRowMapper()
            )
        return result.firstOrNull()
    }

    override fun getCredentialByToken(token: String): Credential? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_CREDENTIAL_BY_TOKEN, MapSqlParameterSource("token", token),
                CredentialRowMapper()
            )
        return result.firstOrNull()
    }

    override fun destroyCredential(credential: Credential): Int {
        TODO("Not yet implemented")
    }

    class CredentialRowMapper : RowMapper<Credential> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Credential {

            return Credential(
                id = DBUtil.getIntValue("id", rs),
                userId = DBUtil.getIntValue("user_id", rs),
                login = DBUtil.getStringValue("login", rs)!!,
                pass = DBUtil.getStringValue("pass", rs)!!,
                token = DBUtil.getStringValue("token", rs),
                tokenExpired = DBUtil.getDateValue("token_expired", rs),
                enabled = DBUtil.getBooleanValue("enabled", rs)!!
            )

        }
    }

}