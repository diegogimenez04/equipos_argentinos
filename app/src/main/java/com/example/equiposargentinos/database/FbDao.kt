package com.example.equiposargentinos.database

import androidx.room.*
import com.example.equiposargentinos.Team

@Dao
interface FbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(fbList: MutableList<Team>)

    @Query("SELECT * FROM teams")
    fun getAllTeams(): MutableList<Team>

    @Update
    fun updateEq(vararg tm: Team)

    @Delete
    fun deleteEq(vararg tm: Team)
}