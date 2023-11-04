package com.itavgur.otushighload.dao

import com.itavgur.otushighload.config.PostgresDBConfig
import com.itavgur.otushighload.domain.Post
import com.itavgur.otushighload.util.DBUtil
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime

@Repository
@ConditionalOnBean(PostgresDBConfig::class)
class PostDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : PostDao {
    companion object {

        const val QUERY_GET_POST_BY_ID = """SELECT id ,text, author_id,status,created,updated
                FROM posts WHERE id=:postId AND author_id = :userId"""

        const val QUERY_GET_POSTS_OF_FRIENDS =
            """SELECT id,text,author_id,status,created,updated 
                FROM posts WHERE author_id IN (:friendIds) OFFSET :offset """

        const val QUERY_GET_POSTS_OF_FRIENDS_LIMITED = """SELECT id,text,author_id,status,created,updated FROM posts 
                WHERE author_id IN (:friendIds) 
                ORDER BY updated DESC 
                OFFSET :offset LIMIT :limit """

        const val QUERY_INSERT_POST =
            """INSERT INTO posts (text, author_id, status, created, updated) VALUES (:text, :author_id, :status, :created, :updated)"""

        const val QUERY_UPDATE_POST =
            """UPDATE posts SET text=:text,updated=:updated WHERE id=:postId AND author_id=:user_id"""

        const val QUERY_DELETE_POST = """DELETE FROM posts WHERE id = :postId AND author_id=:userId"""
    }

    class PostRowMapper : RowMapper<Post> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Post {

            return Post(
                id = DBUtil.getIntValue("id", rs),
                text = DBUtil.getStringValue("text", rs)!!,
                authorId = DBUtil.getIntValue("author_id", rs),
                status = DBUtil.getStringValue("status", rs)!!,
                created = DBUtil.getDateTimeValue("created", rs),
                updated = DBUtil.getDateTimeValue("created", rs),
            )
        }
    }

    override fun getPost(id: Int, userId: Int): Post? {
        return namedParameterJdbcTemplate.query(
            QUERY_GET_POST_BY_ID, mapOf("postId" to id, "userId" to userId), PostRowMapper()
        ).ifEmpty { null }?.get(0)
    }

    override fun createPost(post: Post): Post {

        post.created = LocalDateTime.now()
        post.updated = post.created

        val map = MapSqlParameterSource(
            mapOf(
                "text" to post.text, "author_id" to post.authorId, "status" to post.status,
                "created" to post.created, "updated" to post.updated
            )
        )

        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_POST, map, generatedKeyHolder)

        post.id = generatedKeyHolder.keyList.first()["id"] as Int?
        return post.clone()
    }

    override fun updatePost(post: Post): Post {

        post.updated = LocalDateTime.now()

        val map = MapSqlParameterSource(
            mapOf(
                "postId" to post.id, "user_id" to post.authorId,
                "text" to post.text, "updated" to post.updated,
            )
        )

        namedParameterJdbcTemplate.update(QUERY_UPDATE_POST, map)
        return post
    }

    override fun deletePost(id: Int, userId: Int): Int {
        return namedParameterJdbcTemplate.update(QUERY_DELETE_POST, mapOf("postId" to id, "userId" to userId))
    }

    override fun feedPosts(offset: Int, limit: Int?, friendIds: Set<Int>): List<Post> {

        val map = MapSqlParameterSource(
            mapOf("offset" to offset, "friendIds" to friendIds, "limit" to limit)
        )

        limit?.let {
            return namedParameterJdbcTemplate.query(QUERY_GET_POSTS_OF_FRIENDS_LIMITED, map, PostRowMapper())
        }
        return namedParameterJdbcTemplate.query(QUERY_GET_POSTS_OF_FRIENDS, map, PostRowMapper())

    }

    override fun getPostsByUserId(userId: Int): Set<Post> {
        TODO("Not yet implemented")
    }
}