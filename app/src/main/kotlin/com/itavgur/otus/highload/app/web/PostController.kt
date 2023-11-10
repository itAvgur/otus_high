package com.itavgur.otus.highload.app.web

import com.itavgur.otus.highload.app.aspect.Authenticated
import com.itavgur.otus.highload.app.service.CredentialService
import com.itavgur.otus.highload.app.service.PostService
import com.itavgur.otus.highload.app.web.dto.PostDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/post")
@Schema(description = "User's post")
class PostController(
    @Autowired val postService: PostService,
    @Autowired val credentialService: CredentialService
) {

    @Operation(summary = "Get post", description = "Get post by id")
    @GetMapping("/{id}")
    @Authenticated
    fun getPostById(
        @PathVariable(name = "id", required = true) id: Int,
        @RequestParam(name = "token", required = true) token: String
    ): PostDto {
        val credential = credentialService.getCredentialByToken(token)
        return postService.getPost(id, credential.userId!!)
    }

    @Operation(summary = "Create post", description = "Create new post")
    @PostMapping
    @Authenticated
    fun createNewPost(
        @Valid @RequestBody request: PostDto,
        @RequestParam(name = "token", required = true) token: String
    ): PostDto {
        val credential = credentialService.getCredentialByToken(token)
        return postService.savePost(request, credential.userId!!)
    }

    @Operation(summary = "Update post", description = "Update post")
    @PutMapping
    @Authenticated
    fun updatePost(
        @Valid @RequestBody request: PostDto,
        @RequestParam(name = "token", required = true) token: String
    ): PostDto {
        val credential = credentialService.getCredentialByToken(token)
        return postService.savePost(request, credential.userId!!)
    }

    @Operation(summary = "Delete post", description = "Delete post by id")
    @DeleteMapping("/{id}")
    @Authenticated
    fun deleteFriendById(
        @PathVariable(name = "id", required = true) id: Int,
        @RequestParam(name = "token", required = true) token: String
    ) {
        val credential = credentialService.getCredentialByToken(token)
        postService.deletePost(id, credential.userId!!)
    }

    @Operation(summary = "Feed post", description = "Get posts of friends")
    @GetMapping("/feed")
    @Authenticated
    fun feedPost(
//todo        @RequestParam(name = "offset", defaultValue = "0", required = false) offset: Int,
//todo        @RequestParam(name = "limit", required = false) limit: Int?,
        @RequestParam(name = "token", required = true) token: String
    ): List<PostDto> {
        val credential = credentialService.getCredentialByToken(token)
        return postService.feedPosts(userId = credential.userId!!)
    }

}