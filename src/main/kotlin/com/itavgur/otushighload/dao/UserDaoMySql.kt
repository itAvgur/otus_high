package com.itavgur.otushighload.dao

import com.itavgur.otushighload.config.MysqlDBConfig
import com.itavgur.otushighload.domain.City
import com.itavgur.otushighload.domain.Gender
import com.itavgur.otushighload.domain.User
import com.itavgur.otushighload.util.DBUtil.Companion.getIntValue
import com.itavgur.otushighload.util.DBUtil.Companion.getStringValue
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet


@Repository
@ConditionalOnBean(MysqlDBConfig::class)
class UserDaoMySql(
    private val jdbcTemplate: JdbcTemplate,
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    private val cityDao: CityDaoMySql
) : UserDao {
    companion object {

        const val QUERY_GET_ALL_USERS =
            """SELECT users.id ,users.first_name, users.last_name, users.age,users.gender, c.id as city_id, c.name as city_name
                FROM users JOIN cities c on c.id = users.city_id"""

        const val QUERY_GET_USER_BY_ID =
            """SELECT users.id ,users.first_name, users.last_name, users.age,users.gender, c.id as city_id, c.name as city_name
                FROM users JOIN cities c on c.id = users.city_id
                where users.id = :userId"""

        const val QUERY_INSERT_USER =
            """INSERT INTO users (users.first_name, users.last_name, users.age, users.gender, users.city_id)
                VALUES (:firstName, :lastName, :age, :gender, :cityId)"""

        const val QUERY_UPDATE_USER =
            """UPDATE users
                SET first_name = :firstName, last_name = :lastName, age = :age, gender = :gender, city_id = :cityId
                WHERE users.id = :id"""

    }

    override fun getUsers(): Set<User> {

        val map = MapSqlParameterSource()

        val result = namedParameterJdbcTemplate.query(QUERY_GET_ALL_USERS, map, UserRowMapper())
        return result.toSet()
    }

    class UserRowMapper : RowMapper<User> {
        override fun mapRow(rs: ResultSet, rowNum: Int): User {

            return User(
                id = getIntValue("id", rs),
                firstName = getStringValue("first_name", rs)!!,
                lastName = getStringValue("last_name", rs)!!,
                age = getIntValue("age", rs)!!,
                gender = Gender.valueOf(getStringValue("gender", rs)!!),
                city = City(getIntValue("city_id", rs)!!, getStringValue("city_name", rs)!!)
            )

        }
    }

    override fun getUserById(id: Int): User? {
        val result =
            namedParameterJdbcTemplate.query(QUERY_GET_USER_BY_ID, MapSqlParameterSource("userId", id), UserRowMapper())
        return result.firstOrNull()
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = [Exception::class, RuntimeException::class])
    override fun createUser(user: User): User {

        user.city?.let {
            user.city.id = cityDao.saveCity(user.city)
        }

        val map = MapSqlParameterSource(
            mapOf(
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "age" to user.age,
                "gender" to user.gender.toString(),
                "cityId" to user.city?.id
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_USER, map, generatedKeyHolder)
        user.id = generatedKeyHolder.key?.toInt()

        return user
    }

    override fun updateUser(user: User): User {

        user.city?.let {
            user.city.id = cityDao.saveCity(user.city)
        }

        val map = MapSqlParameterSource(
            mapOf(
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "age" to user.age,
                "gender" to user.gender.toString(),
                "cityId" to user.city?.id
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_UPDATE_USER, map, generatedKeyHolder)
        user.id = generatedKeyHolder.key?.toInt()

        return user
    }

    override fun deleteUser(id: Int): Int {
        TODO("Not yet implemented")
    }

    override fun searchUsersByFirstNameAndLastName(firstName: String?, lastName: String?): Set<User> {
        TODO("Not yet implemented")
    }
}

@Repository
@ConditionalOnBean(MysqlDBConfig::class)
class CityDaoMySql(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

    companion object {

        const val QUERY_GET_CITY_BY_NAME =
            """SELECT cities.id ,cities.name FROM cities where cities.name = :cityName"""

        const val QUERY_INSERT_CITY = """INSERT INTO cities (cities.name) VALUES (:cityName)"""
    }

    fun saveCity(city: City): Int {

        getCityByName(city)?.let {
            return it.id
        }
        return insertCity(city)
    }

    fun getCityByName(city: City): City? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_GET_CITY_BY_NAME, MapSqlParameterSource("cityName", city.name),
                CityRowMapper()
            )
        return result.firstOrNull()
    }

    private fun insertCity(city: City): Int {
        val generatedKeyHolder = GeneratedKeyHolder()
        val map = MapSqlParameterSource("cityName", city.name)
        namedParameterJdbcTemplate.update(QUERY_INSERT_CITY, map, generatedKeyHolder)
        return generatedKeyHolder.key?.toInt()!!
    }

    class CityRowMapper : RowMapper<City> {
        override fun mapRow(rs: ResultSet, rowNum: Int): City {

            return City(
                id = getIntValue("id", rs)!!,
                name = getStringValue("name", rs)!!
            )

        }
    }

}