package com.itavgur.otushighload.dao

import com.itavgur.otushighload.domain.City
import com.itavgur.otushighload.domain.Gender
import com.itavgur.otushighload.domain.Hobby
import com.itavgur.otushighload.domain.User

class UserDaoMock : UserDao {

    private val sqlTable: MutableSet<User> = initTable()

    override fun getUsers(): MutableSet<User> = sqlTable

    override fun getUserById(id: Int): User? = sqlTable.firstOrNull { it.id == id }

    override fun createUser(user: User): User {

        var res: User
        sqlTable
            .sortedBy { it.id }
            .last()
            .let {
                res = user.clone()
                res.id = it.id?.plus(1)
                sqlTable.add(res)
            }
        return res
    }

    override fun updateUser(user: User): User {

        sqlTable.firstOrNull { it.id == user.id }
            ?.let {
                sqlTable.remove(it)
                sqlTable.add(user)
            }

        return user.clone()
    }

    override fun deleteUser(id: Int): Int {

        var deletedEntries: Int = 0
        sqlTable
            .firstOrNull { it.id == id }
            ?.let {
                sqlTable.remove(it)
                ++deletedEntries
            }
        return deletedEntries
    }

    private fun initTable(): MutableSet<User> {

        val mockUser01 = User(
            id = 1,
            firstName = "John", lastName = "Smith", age = 10,
            gender = Gender.MALE, city = City(0, "IKT"),
            hobbies = listOf(Hobby(1, "hobby-01"), Hobby(2, "hobby-02"))
        )
        val mockUser02 = User(
            id = 2,
            firstName = "Harrison", lastName = "Ford", age = 20,
            gender = Gender.FEMALE, city = City(1, "MSK"),
            hobbies = listOf(Hobby(3, "hobby-03"), Hobby(4, "hobby-04"))
        )

        return mutableSetOf(mockUser01, mockUser02)
    }

}