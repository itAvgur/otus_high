package com.itavgur.otushighload.web.dto

import com.itavgur.otushighload.domain.Post
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(name = "Post", description = "Post of user")
data class PostDto(
    @Schema(name = "id", required = false)
    var id: Int? = null,
    @Schema(name = "text", required = true)
    val text: String,
    @Schema(name = "authorId", required = false)
    var authorId: Int? = null

) : Serializable {

    fun toPost(): Post {
        return Post(
            id = id,
            text = text,
            authorId = authorId
        )
    }

    companion object {
        fun from(post: Post): PostDto {
            return PostDto(
                id = post.id,
                text = post.text,
                authorId = post.authorId
            )
        }
    }
}