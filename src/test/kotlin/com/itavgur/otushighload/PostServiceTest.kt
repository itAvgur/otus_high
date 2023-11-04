package com.itavgur.otushighload

import com.itavgur.otushighload.BaseTest.MockitoHelper.anyObject
import com.itavgur.otushighload.dao.PostDaoMock
import com.itavgur.otushighload.domain.Post
import com.itavgur.otushighload.exception.PostNotFoundException
import com.itavgur.otushighload.service.FriendService
import com.itavgur.otushighload.service.PostService
import com.itavgur.otushighload.web.dto.PostDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.anyInt
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class PostServiceTest {

    @Mock
    private lateinit var postDao: PostDaoMock

    @Mock
    private lateinit var friendService: FriendService

    @InjectMocks
    private lateinit var postService: PostService

    private val userId = 1

    private val mockPost01 = Post(id = 1, text = "Post number one", authorId = userId)
    private val mockPost01Updated = Post(id = 1, text = "Post number one_updated", authorId = userId)
    private val mockPost02 = Post(id = 2, text = "Post number two", authorId = userId)
    private val mockPost03 = Post(id = 3, text = "Post number three", authorId = 666)


    private val newPostDto01 = PostDto(text = "this is a text")
    private val postDto01Updated = PostDto(id = 1, text = "Post number one_updated")

    //GET
    @Test
    fun givenValidUserAndPost_whenGetPost_thenGetThisPost() {
        given(postDao.getPost(mockPost02.id!!, userId)).willReturn(mockPost02)
        val actual = postService.getPost(mockPost02.id!!, userId)
        assertEquals(PostDto.from(mockPost02), actual)
    }

    @Test
    fun givenNotValidUser_whenGetPost_thenThrowException() {
        assertThrows<PostNotFoundException> {
            postService.getPost(mockPost03.id!!, userId)
        }
    }

    @Test
    fun givenNotValidPost_whenGetPost_thenGetThisPost() {
        given(postDao.getPost(mockPost01.id!!, userId)).willReturn(null)
        assertThrows<PostNotFoundException> {
            postService.getPost(mockPost01.id!!, userId)
        }
    }

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

}