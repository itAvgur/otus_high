package com.itavgur.otus.highload.app.web.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.itavgur.otus.highload.app.domain.Post
import com.itavgur.otus.highload.app.util.NoArgConstructor
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(name = "Post", description = "Post of user")
@NoArgConstructor
data class PostDto(
    @Schema(name = "id", required = false)
    var id: Int? = null,
    @Schema(name = "text", required = true)
    val text: String,
    @Schema(name = "authorId", required = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
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