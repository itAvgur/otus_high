package com.itavgur.otus.highload.app

import com.itavgur.otus.highload.app.dao.MessageDao
import com.itavgur.otus.highload.app.domain.Credential
import com.itavgur.otus.highload.app.domain.Message
import com.itavgur.otus.highload.app.exception.MessageNotFoundException
import com.itavgur.otus.highload.app.service.CredentialService
import com.itavgur.otus.highload.app.service.MessageService
import com.itavgur.otus.highload.app.web.dto.MessageRequestDto
import com.itavgur.otus.highload.app.web.dto.MessageResponseDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class MessageServiceTest {

    @Mock
    private lateinit var messageDao: MessageDao

    @Mock
    private lateinit var credentialService: CredentialService

    @InjectMocks
    private lateinit var messageService: MessageService

    private val userId1 = 1
    private val userId2 = 1
    private val user2Login = "user_2_login"
    private val user2Credential = Credential(userId = userId2, login = "login_valid", pass = "pass", enabled = true)

    private val mockMessage01 = Message(id = 1, text = "Message number one", userFromId = userId1, userToId = userId2)
    private val mockMessage02 = Message(id = 2, text = "Message number two", userFromId = userId2, userToId = userId1)
    private val newMessage01 = Message(text = "New message", userFromId = userId1, userToId = userId2)
    private val newMessageWithId01 = Message(id = 3, text = "New message", userFromId = userId1, userToId = userId2)

    @Test
    fun givenValidUserAndMessage_whenGetMessage_thenGetThisMessage() {
        given(messageDao.getMessage(mockMessage01.id!!, userId2)).willReturn(mockMessage01)
        val actual = messageService.getMessage(userId1, userId2)
        assertEquals(MessageResponseDto.from(mockMessage01), actual)
    }

    @Test
    fun givenNotValidUserOrNotValidMessageId_whenGetMessage_thenThrowException() {

        assertThrows<MessageNotFoundException> {
            messageService.getMessage(mockMessage01.id!!, userId1)
        }
    }

    @Test
    fun givenValidMessages_whenGetMessages_thenGetThem() {
        given(messageDao.getMessages(mockMessage01.id!!)).willReturn(listOf(mockMessage01, mockMessage02))
        val actual = messageService.getMessages(userId1)
        val expected = listOf(mockMessage01, mockMessage02).map(MessageResponseDto.Companion::from)
        assertEquals(expected, actual)
    }

    @Test
    fun givenNoMessages_whenGetMessages_thenGetEmptyList() {
        val actual = messageService.getMessages(userId1)
        assertTrue(actual.isEmpty())
    }

    @Test
    fun givenValidUsers_whenSendMessage_thenSendMessageAndGetItWithId() {
        given(messageDao.sendMessage(newMessage01)).willReturn(newMessageWithId01)
        given(credentialService.getCredentialByLogin(user2Login)).willReturn(user2Credential)
        val request = MessageRequestDto(text = "New message", userToLogin = user2Login)
        val actual = messageService.sendMessage(request, userId1)
        val expected = MessageResponseDto.from(newMessageWithId01)
        assertEquals(expected.text, actual.text)
        assertEquals(expected.userToId, actual.userToId)
        assertEquals(expected.userFromId, userId1)
    }


    /*

        //CREATE
        @Test
        fun givenValidUser_whenCreatePost_thenGetThisPostWithId() {
            given(postDao.createPost(anyObject())).willReturn(mockPost01)
            val actual = postService.savePost(newPostDto01, userId)
            val expected = PostDto.from(mockPost01)
            assertEquals(expected, actual)
            verify(postDao, times(1)).createPost(anyObject())
            verify(postDao, never()).updatePost(anyObject())
        }

        //UPDATE
        @Test
        fun givenValidUser_whenUpdatePost_thenGetThisPost() {
            given(postDao.getPost(mockPost01Updated.id!!, userId)).willReturn(mockPost01Updated)
            given(postDao.updatePost(anyObject())).willReturn(mockPost01Updated)
            val actual = postService.savePost(postDto01Updated, userId)
            val expected = PostDto.from(mockPost01Updated)
            assertEquals(expected, actual)
            verify(postDao, never()).createPost(anyObject())
            verify(postDao, times(1)).updatePost(anyObject())
        }

        @Test
        fun givenPostNotOur_whenUpdatePost_thenThrowException() {
            assertThrows<PostNotFoundException> {
                postService.savePost(PostDto.from(mockPost03), userId)
            }
            verify(postDao, never()).createPost(anyObject())
            verify(postDao, never()).updatePost(anyObject())
        }


        //DELETE
        @Test
        fun givenValidUser_whenDeletePost_thenDeleteThisPost() {
            given(postDao.getPost(mockPost01.id!!, userId)).willReturn(mockPost01)
            given(postDao.deletePost(mockPost01.id!!, userId)).willReturn(1)
            assertDoesNotThrow { postService.deletePost(mockPost01.id!!, userId) }
        }

        @Test
        fun givenPostNotExist_whenDeletePost_thenThrowException() {
            assertThrows<PostNotFoundException> {
                postService.deletePost(mockPost03.id!!, userId)
            }
            verify(postDao, never()).deletePost(mockPost03.id!!, userId)
        }

        @Test
        fun givenPostNotOur_whenDeletePost_thenThrowException() {
            assertThrows<PostNotFoundException> {
                postService.deletePost(mockPost03.id!!, userId)
            }
            verify(postDao, never()).deletePost(mockPost03.id!!, userId)
        }

        //FEED
        @Test
        fun givenPostExists_whenFeedPost_thenGetPosts() {
            given(friendService.findFriendsOf(anyInt())).willReturn(
                setOf(
                    mockPost01.id!!,
                    mockPost02.id!!,
                    mockPost03.id!!
                )
            )
            given(postDao.feedPosts(anyInt(), anyInt(), anyObject())).willReturn(
                listOf(
                    mockPost01,
                    mockPost02,
                    mockPost03
                )
            )

            val actual = postService.feedPosts(0, Int.MAX_VALUE, userId)
            val expected = listOf(PostDto.from(mockPost01), PostDto.from(mockPost02), PostDto.from(mockPost03))
            assertIterableEquals(expected, actual)
        }


     */
}