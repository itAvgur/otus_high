package com.itavgur.otushighload.web

import com.itavgur.otushighload.aspect.Authenticated
import com.itavgur.otushighload.service.PostService
import com.itavgur.otushighload.web.dto.PostDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/post")
@Schema(description = "User's post")
class PostController(
    @Autowired
    val postService: PostService
) {

    @Operation(summary = "Get post", description = "Get post by id")
    @GetMapping("/{id}")
    @Authenticated
    fun getPostById(
        @PathVariable(name = "id", required = true) id: Int,
        @RequestParam(name = "token", required = true) token: String
    ): PostDto = postService.getPost(id, token)

    @Operation(summary = "Create post", description = "Create new post")
    @PostMapping
    @Authenticated
    fun createNewPost(
        @Valid @RequestBody request: PostDto,
        @RequestParam(name = "token", required = true) token: String
    ): PostDto = postService.savePost(request, token)

    @Operation(summary = "Update post", description = "Update post")
    @PutMapping
    @Authenticated
    fun updatePost(
        @Valid @RequestBody request: PostDto,
        @RequestParam(name = "token", required = true) token: String
    ): PostDto = postService.savePost(request, token)

    @Operation(summary = "Delete post", description = "Delete post by id")
    @DeleteMapping("/{id}")
    @Authenticated
    fun deleteFriendById(
        @PathVariable(name = "id", required = true) id: Int,
        @RequestParam(name = "token", required = true) token: String
    ) = postService.deletePost(id, token)

    @Operation(summary = "Feed post", description = "Get posts of friends")
    @GetMapping("/feed")
    @Authenticated
    fun feedPost(
        @RequestParam(name = "offset", defaultValue = "0", required = false) offset: Int,
        @RequestParam(name = "limit", defaultValue = "10", required = false) limit: Int,
        @RequestParam(name = "token", required = true) token: String
    ): List<PostDto> = postService.feedPosts(offset, limit, token)

}