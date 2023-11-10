package com.itavgur.otus.highload.app.dao

import com.itavgur.otus.highload.app.domain.Credential

interface CredentialDao {

    fun insertCredential(credential: Credential, userId: Int): Credential

    fun updateCredential(credential: Credential): Credential

    fun getCredentialById(id: Int): Credential?

    fun getCredentialByLogin(login: String): Credential?

    fun getCredentialByToken(token: String): Credential?

    fun destroyCredential(credential: Credential): Int

}