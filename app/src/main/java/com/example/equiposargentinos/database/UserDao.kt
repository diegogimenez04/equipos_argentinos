package com.example.equiposargentinos.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.equiposargentinos.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE userId LIKE (:userId)")
    fun loadById(userId: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg user: User)

    @Delete
    fun delete(user: User)
}