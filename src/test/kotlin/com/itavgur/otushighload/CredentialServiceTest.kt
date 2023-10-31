package com.itavgur.otushighload

import com.itavgur.otushighload.BaseTest.MockitoHelper.anyObject
import com.itavgur.otushighload.dao.CredentialDao
import com.itavgur.otushighload.dao.CredentialDaoMock
import com.itavgur.otushighload.domain.Credential
import com.itavgur.otushighload.service.CredentialService
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.annotation.DirtiesContext

@ExtendWith(MockitoExtension::class)
@DirtiesContext
class CredentialServiceTest {

    @Spy
    private val dao: CredentialDao = CredentialDaoMock()

    @Mock
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    @InjectMocks
    private lateinit var credService: CredentialService

    private val credentialValid: Credential = Credential(login = "login_valid", pass = "pass", enabled = true)

    @Test
    fun givenValidCredential_whenCheckCredential_thenGetTrue() {
        BDDMockito.given(passwordEncoder.matches(anyObject(), anyString())).willReturn(true)
        BDDMockito.given(dao.getCredentialByLogin(credentialValid.login)).willReturn(credentialValid)
        assertTrue(credService.checkCredential(credentialValid))
    }

    /*

    @Test
    fun givenNoUsers_whenGetUsers_thenGetEmptyList() {

        BDDMockito.given(userDao.getUsers()).willReturn(emptyList())

        val actual = userService.getUsers()
        val expected = emptyList<UserDto>()

        assertEquals((expected), actual)
    }

    @Test
    fun givenValidUsers_whenGetUserById1_thenGetThisUser() {
        val actual = userService.getUser(1)
        val expected = UserDto.from(UserDaoMock.mockUser01)
        assertEquals((expected), actual)
    }

    @Test
    fun givenValidUsers_whenGetUserById2_thenGetThisUser() {
        val actual = userService.getUser(2)
        val expected = UserDto.from(UserDaoMock.mockUser02)
        assertEquals((expected), actual)
    }

    @Test
    fun givenValidUser_whenGetUserById3_thenThrowException() {

        assertThrows<UserNotFoundException> { userService.getUser(3) }
    }

    @Test
    fun givenUserNotExist_whenSaveUser_thenReturnUser() {

        val userDto = UserDto(
            firstName = "John", lastName = "Malkovich", age = 10,
            gender = Gender.MALE, city = City("IKT"),
            hobbies = listOf(Hobby("hobby-01"), Hobby("hobby-02"))
        )

        val result = userService.saveUser(userDto)

        assertNotNull(result.id)
        BDDMockito.verify(userDao, times(1)).createUser(userDto.toUser())
        BDDMockito.verify(userDao, never()).updateUser(userDto.toUser())
    }

    @Test
    fun givenUserExist_whenSaveUser_thenReturnUser() {

        val userDto = UserDto(
            id = 1,
            firstName = "John", lastName = "Malkovich", age = 10,
            gender = Gender.MALE, city = City("IKT"),
            hobbies = listOf(Hobby("hobby-01"), Hobby("hobby-02"))
        )

        val result = userService.saveUser(userDto)

        assertEquals(userDto.id, result.id)
        BDDMockito.verify(userDao, never()).createUser(userDto.toUser())
        BDDMockito.verify(userDao, times(1)).updateUser(userDto.toUser())
    }


    @Test
    fun givenUserExist_whenDeleteUser_thenReturnInt() {

        assertEquals(1, userService.deleteUser(1))
    }

    @Test
    fun givenUserNotExist_whenDeleteUser_thenReturnZero() {

        BDDMockito.given(userDao.deleteUser(anyInt())).willThrow(UserNotFoundException::class.java)

        assertThrows<UserNotFoundException> { userService.deleteUser(1) }
    }
*/
}
