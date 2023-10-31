package com.itavgur.otushighload.domain

data class Post(
    var id: Int? = null,
    val text: String,
    var authorId: Int?
) : Cloneable {

    public override fun clone(): Post = Post(
        id = id,
        text = text,
        authorId = authorId
    )
}