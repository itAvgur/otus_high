package com.itavgur.otus.highload.app

import com.itavgur.otus.highload.app.dao.FriendDao
import com.itavgur.otus.highload.app.dao.FriendDaoMock
import com.itavgur.otus.highload.app.domain.*
import com.itavgur.otus.highload.app.exception.CredentialException
import com.itavgur.otus.highload.app.exception.InvalidRequestException
import com.itavgur.otus.highload.app.exception.UserNotFoundException
import com.itavgur.otus.highload.app.service.CredentialService
import com.itavgur.otus.highload.app.service.FriendService
import com.itavgur.otus.highload.app.service.PostService
import com.itavgur.otus.highload.app.service.UserService
import com.itavgur.otus.highload.app.web.dto.UserDto
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FriendServiceTest {

    @Mock
    private val friendDao: FriendDao = FriendDaoMock()

    @Mock
    private lateinit var credentialService: CredentialService

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var postService: PostService

    @InjectMocks
    private lateinit var friendService: FriendService

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

    private val mockUser03 = User(
        id = 3,
        firstName = "Friend", lastName = "All", age = 22,
        gender = Gender.FEMALE, city = City(1, "MSK")
    )

    private val token = "this_is_a_token"

    private val userId = 1

    private val credential: Credential = Credential(userId = userId, login = "", pass = "", enabled = true)

    @Test
    fun givenValidFriend_whenGetFriend_thenReturnFriend() {
        val friend = Friend(0, 1, 3)
        given(credentialService.getCredentialByToken(token)).willReturn(credential)
        given(friendDao.getFriend(1, 3)).willReturn(friend)
        assertNotNull(friendService.getFriend(3, token))
    }

    @Test
    fun givenFriendNotExist_whenGetFriend_thenThrowException() {
        given(credentialService.getCredentialByToken(token)).willReturn(credential)
        assertThrows<InvalidRequestException> {
            friendService.getFriend(1, token)
        }
    }

    @Test
    fun givenValidUsers_whenSaveFriend_thenSaveFriend() {
        given(credentialService.getCredentialByToken(token)).willReturn(credential)
        given(userService.getUser(mockUser02.id!!)).willReturn(UserDto.from(mockUser02))
        assertDoesNotThrow {
            friendService.setFriend(mockUser02.id!!, token)
        }
    }

    @Test
    fun givenNotValidUser_whenSaveFriend_thenThrowException() {
        given(credentialService.getCredentialByToken(token)).willThrow(CredentialException(""))
        assertThrows<UserNotFoundException> {
            friendService.setFriend(mockUser02.id!!, token)
        }
    }

    @Test
    fun givenNotValidFriend_whenSaveFriend_thenThrowException() {
        given(credentialService.getCredentialByToken(token)).willReturn(credential)
        given(userService.getUser(mockUser02.id!!)).willThrow(UserNotFoundException(""))
        assertThrows<UserNotFoundException> {
            friendService.setFriend(mockUser02.id!!, token)
        }
    }

    @Test
    fun givenValidUsers_whenDeleteFriend_thenDeleteFriend() {
        given(credentialService.getCredentialByToken(token)).willReturn(credential)
        given(userService.getUser(mockUser02.id!!)).willReturn(UserDto.from(mockUser02))
        assertDoesNotThrow {
            friendService.deleteFriend(mockUser02.id!!, token)
        }
    }

    @Test
    fun givenNotValidUser_whenDeleteFriend_thenThrowException() {
        given(credentialService.getCredentialByToken(token)).willThrow(CredentialException(""))
        assertThrows<UserNotFoundException> {
            friendService.deleteFriend(mockUser02.id!!, token)
        }
    }

    @Test
    fun givenNotValidFriend_whenDeleteFriend_thenThrowException() {
        given(userService.getUser(mockUser02.id!!)).willThrow(UserNotFoundException(""))
        assertThrows<UserNotFoundException> {
            friendService.deleteFriend(mockUser02.id!!, token)
        }
    }

    @Test
    fun givenFriends_whenFindFriends_thenGetIds() {
        given(friendDao.findFriendIds(mockUser01.id!!)).willReturn(setOf(mockUser02.id!!, mockUser03.id!!))
        val expected = setOf(mockUser02.id, mockUser03.id)
        assertIterableEquals(expected, friendService.findFriendsOf(mockUser01.id!!))
    }

}