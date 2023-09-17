package com.itavgur.otushighload.dao

import com.itavgur.otushighload.domain.User

interface UserDao {

    fun getUsers(): Set<User>

    fun getUserById(id: Int): User?

    fun createUser(user: User): User

    fun updateUser(user: User): User

    fun deleteUser(id: Int): Int
}