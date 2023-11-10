package com.itavgur.otus.highload.app.dao

import com.itavgur.otus.highload.app.domain.User

interface UserDao {

    fun getUsers(): Set<User>

    fun getUserById(id: Int): User?

    fun createUser(user: User): User

    fun updateUser(user: User): User

    fun deleteUser(id: Int): Int

    fun searchUsersByFirstNameAndLastName(firstName: String?, lastName: String?): Set<User>
}