package com.itavgur.otushighload.dao

import com.itavgur.otushighload.domain.Credential

class CredentialDaoMock : CredentialDao {

    private val sqlTable: MutableSet<Credential> = initTable()

    override fun insertCredential(credential: Credential, userId: Int): Credential {

        val newCredential: Credential = credential.clone()
        val lastElement = sqlTable.sortedBy { it.id }
            .lastOrNull()

        if (lastElement == null) {
            newCredential.id = 0
        } else {
            newCredential.id = lastElement.id!! + 1
        }
        newCredential.userId = userId
        sqlTable.add(newCredential)
        return newCredential
    }

    override fun updateCredential(credential: Credential): Credential {
        sqlTable.firstOrNull { it.id == credential.id }
            ?.let {
                sqlTable.remove(it)
                sqlTable.add(credential)
            }

        return credential
    }

    override fun getCredentialById(id: Int): Credential? = sqlTable.firstOrNull { it.id == id }

    override fun getCredentialByLogin(login: String): Credential? = sqlTable.firstOrNull { it.login == login }

    override fun getCredentialByToken(token: String): Credential? = sqlTable.firstOrNull { it.token == token }

    override fun destroyCredential(credential: Credential): Int {

        var deletedEntries = 0
        sqlTable
            .firstOrNull { it.id == credential.id }
            ?.let {
                sqlTable.remove(it)
                ++deletedEntries
            }
        return deletedEntries
    }

    private fun initTable(): MutableSet<Credential> {

        val mockCredentialUser01 = Credential(
            id = 1, userId = 1, login = "user01", pass = "pass01", enabled = true
        )
        val mockCredentialUser02 = Credential(
            id = 2, userId = 2, login = "user02", pass = "pass02", enabled = true
        )

        return mutableSetOf(mockCredentialUser01, mockCredentialUser02)
    }

}