package com.example.equiposargentinos.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.equiposargentinos.Team

@Dao
interface FbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(fbList: MutableList<Team>)

    @Query("SELECT * FROM teams")
    fun getAllTeams(): LiveData<MutableList<Team>>

    @Query("SELECT * FROM teams WHERE strTeam LIKE '%'+:nombre+'%'")
    fun getTeamsByName(nombre: String): MutableList<Team>

    @Update
    fun updateFb(vararg tm: Team)

    @Delete
    fun deleteFb(vararg tm: Team)
}