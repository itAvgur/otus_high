package com.itavgur.otushighload

import com.itavgur.otushighload.MockitoHelper.anyObject
import com.itavgur.otushighload.dao.UserDao
import com.itavgur.otushighload.dao.UserDaoMock
import com.itavgur.otushighload.domain.*
import com.itavgur.otushighload.exception.UserNotFoundException
import com.itavgur.otushighload.service.CredentialService
import com.itavgur.otushighload.service.UserService
import com.itavgur.otushighload.web.dto.UserDto
import com.itavgur.otushighload.web.dto.UserRegistrationRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.annotation.DirtiesContext

@ExtendWith(MockitoExtension::class)
@DirtiesContext
class UserServiceTest {

    @Spy
    private val userDao: UserDao = UserDaoMock()

    @Mock
    private lateinit var credentialService: CredentialService

    private lateinit var userService: UserService

    private val mockUser01 = User(
        id = 1,
        firstName = "John", lastName = "Smith", age = 10,
        gender = Gender.MALE, city = City(0, "IKT"),
        hobbies = listOf(Hobby(1, "hobby-01"), Hobby(2, "hobby-02"))
    )
    private val mockUser02 = User(
        id = 2,
        firstName = "Harrison", lastName = "Ford", age = 20,
        gender = Gender.FEMALE, city = City(1, "MSK"),
        hobbies = listOf(Hobby(3, "hobby-03"), Hobby(4, "hobby-04"))
    )

    @BeforeEach
    fun init() {
        userService = UserService(userDao, credentialService)
    }

    @Test
    fun givenValidUsers_whenGetUsers_thenGetThisUsers() {
        val actual = userService.getUsers()
        val expected = listOf(UserDto.from(mockUser01), UserDto.from(mockUser02))
        assertEquals((expected), actual)
    }

    @Test
    fun givenNoUsers_whenGetUsers_thenGetEmptyList() {

        given(userDao.getUsers()).willReturn(emptySet())

        val actual = userService.getUsers()
        val expected = emptyList<UserDto>()

        assertEquals((expected), actual)
    }

    @Test
    fun givenValidUsers_whenGetUserById1_thenGetThisUser() {
        val actual = userService.getUser(1)
        val expected = UserDto.from(mockUser01)
        assertEquals((expected), actual)
    }

    @Test
    fun givenValidUsers_whenGetUserById2_thenGetThisUser() {
        val actual = userService.getUser(2)
        val expected = UserDto.from(mockUser02)
        assertEquals((expected), actual)
    }

    @Test
    fun givenValidUser_whenGetUserById3_thenThrowException() {

        assertThrows<UserNotFoundException> { userService.getUser(3) }
    }

    @Test
    fun givenUserNotExist_whenSaveUser_thenReturnUser() {

        val userDto = UserDto(
            firstName = "John", lastName = "Smith", age = 10,
            gender = Gender.MALE, city = City(0, "IKT"),
            hobbies = listOf(Hobby(1, "hobby-01"), Hobby(2, "hobby-02"))
        )

        val result = userService.saveUser(userDto)

        assertNotNull(result.id)
        verify(userDao, times(1)).createUser(userDto.toUser())
        verify(userDao, never()).updateUser(userDto.toUser())
    }

    @Test
    fun givenUserExist_whenSaveUser_thenReturnUser() {

        val userDto = UserDto(
            id = 1,
            firstName = "John", lastName = "Smith", age = 10,
            gender = Gender.MALE, city = City(0, "IKT"),
            hobbies = listOf(Hobby(1, "hobby-01"), Hobby(2, "hobby-02"))
        )

        val result = userService.saveUser(userDto)

        assertEquals(userDto.id, result.id)
        verify(userDao, never()).createUser(userDto.toUser())
        verify(userDao, times(1)).updateUser(userDto.toUser())
    }


    @Test
    fun givenUserExist_whenDeleteUser_thenReturnInt() {

        assertEquals(1, userService.deleteUser(1))
    }

    @Test
    fun givenUserNotExist_whenDeleteUser_thenReturnZero() {

        given(userDao.deleteUser(anyInt())).willThrow(UserNotFoundException::class.java)
        assertThrows<UserNotFoundException> { userService.deleteUser(1) }
    }

    @Test
    fun givenUserNotExist_whenRegisterUser_thenReturnUser() {

        val userDto = UserDto(
            firstName = "John", lastName = "Smith", age = 10,
            gender = Gender.MALE, city = City(0, "IKT"),
            hobbies = listOf(Hobby(1, "hobby-01"), Hobby(2, "hobby-02"))
        )
        val request = UserRegistrationRequest(login = "login", pass = "pass", passRepeat = "pass", user = userDto)
        val cred = Credential(login = request.login, pass = request.pass, enabled = true)

        given(credentialService.checkCredential(anyObject())).willReturn(false)
        given(credentialService.saveCredential(anyObject(), anyInt())).willReturn(cred)
        val savedUser = userDto.toUser()
        savedUser.id = 0
        given(userDao.createUser(userDto.toUser())).willReturn(savedUser)

        assertEquals(UserDto.from(savedUser), userService.registerUser(request))
    }

}

/*
https://stackoverflow.com/questions/59230041/argumentmatchers-any-must-not-be-null
 */
object MockitoHelper {
    fun <T> anyObject(): T {
        any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T
}
