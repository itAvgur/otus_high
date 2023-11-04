package com.itavgur.otushighload.domain

import com.itavgur.otushighload.util.NoArgConstructor
import java.time.LocalDateTime

@NoArgConstructor
data class Post(
    var id: Int? = null,
    val text: String,
    var authorId: Int?,
    var status: String = "OPEN",
    var created: LocalDateTime? = null,
    var updated: LocalDateTime? = null
) : Cloneable {

    public override fun clone(): Post = Post(
        id = id,
        text = text,
        authorId = authorId,
        status = status,
        created = created,
        updated = updated
    )
}