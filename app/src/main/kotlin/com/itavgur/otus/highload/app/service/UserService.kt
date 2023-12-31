package com.itavgur.otus.highload.app.service

import com.itavgur.otus.highload.app.dao.UserDao
import com.itavgur.otus.highload.app.domain.Credential
import com.itavgur.otus.highload.app.exception.InvalidRequestException
import com.itavgur.otus.highload.app.exception.UserNotFoundException
import com.itavgur.otus.highload.app.web.dto.UserDto
import com.itavgur.otus.highload.app.web.dto.UserRegistrationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userDao: UserDao,
    @Autowired private val credentialService: CredentialService
) {

    companion object {
        const val USER_ENABLED_DEFAULT = true
        const val TOP_ACTIVE_USERS_AMOUNT = 500L
    }

    fun getUsers(): List<UserDto> = userDao.getUsers().stream()
        .map { UserDto.from(it) }
        .toList()

    fun getUser(id: Int): UserDto {
        userDao.getUserById(id)
            ?.let { return UserDto.from(it) }
        throw UserNotFoundException("user with id $id missed")
    }

    fun saveUser(userDto: UserDto): UserDto {

        userDto.id?.let {
            userDao.getUserById(userDto.id!!)
                ?.let {
                    return UserDto.from(userDao.updateUser(user = userDto.toUser()))
                }
            throw UserNotFoundException("user with id ${userDto.id} missed")
        }

        val result = userDao.createUser(user = userDto.toUser())
        return UserDto.from(result)
    }

    fun deleteUser(id: Int): Int {

        userDao.getUserById(id)
            ?.let {
                return userDao.deleteUser(id)
            }
        throw UserNotFoundException("user with id ${id} missed")
    }

    fun registerUser(request: UserRegistrationRequest): UserDto {

        validateRegisterUserRequest(request)

        val credential = Credential(
            login = request.login,
            pass = request.pass,
            enabled = USER_ENABLED_DEFAULT
        )
        if (credentialService.checkCredential(credential)) {
            throw InvalidRequestException("login is already exists")
        }

        //TODO add transaction
        val newUser = saveUser(request.user)
        credentialService.saveCredential(credential, newUser.id!!)
        return newUser
    }

    @Throws(InvalidRequestException::class)
    private fun validateRegisterUserRequest(request: UserRegistrationRequest) {
        if (request.user.id != null) {
            throw InvalidRequestException("user id must be empty")
        }
        if (request.pass != request.passRepeat) {
            throw InvalidRequestException("passwords must be equal")
        }

    }

    fun searchUsers(firstName: String?, lastName: String?): List<UserDto> {
        if (firstName.isNullOrBlank() && lastName.isNullOrBlank()) {
            throw InvalidRequestException("both firstName and lastName are empty")
        }
        return userDao.searchUsersByFirstNameAndLastName(firstName, lastName).stream()
            .map { UserDto.from(it) }
            .toList()
    }

    //todo implement statistics later
    fun getTopActiveUsers(): List<UserDto> = userDao.getUsers().stream()
        .limit(TOP_ACTIVE_USERS_AMOUNT)
        .map { UserDto.from(it) }
        .toList()

}