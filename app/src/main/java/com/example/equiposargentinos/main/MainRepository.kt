package com.example.equiposargentinos.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.equiposargentinos.Team
import com.example.equiposargentinos.api.FbJsonResponse
import com.example.equiposargentinos.api.service
import com.example.equiposargentinos.database.FbDatabase
import com.example.equiposargentinos.database.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(private val database: FbDatabase, private val userDatabase: UserDatabase) {

    val fbList: LiveData<MutableList<Team>> = database.fbDao.getAllTeams()

    suspend fun fetchTeams() {
        return withContext(Dispatchers.IO) {
            val fbJsonResponse = service.getTeams()
            val fbList = parseTeamResult(fbJsonResponse)

            database.fbDao.insertAll(fbList)
        }
    }

    suspend fun fetchTeamsWithName(name: String): MutableList<Team> {
        return withContext(Dispatchers.IO) {
            database.fbDao.getTeamsByName(name)
        }
    }

    private fun parseTeamResult(fbJsonResponse: FbJsonResponse): MutableList<Team> {
        val fbList = mutableListOf<Team>()

        val teams = fbJsonResponse.teams

        for (team in teams){
            val teamId = team.idTeam
            val name = team.strTeam ?: ""
            val stadiumName = team.strStadium ?: ""
            val stadiumLoc = team.strStadiumLocation ?: ""
            val stadiumCap = team.intStadiumCapacity ?: 0
            val stadiumImg = team.strStadiumThumb ?: ""
            val teamBadge = team.strTeamBadge
            val teamAbrv = team.strTeamShort ?: ""
            val teamWebsite = team.strWebsite ?: ""

            fbList.add(Team(teamId, name, teamAbrv, stadiumName, stadiumImg, stadiumLoc,
                        stadiumCap, teamWebsite,teamBadge, false))
        }

        return fbList
    }

    suspend fun fetchUsers() {
        return withContext(Dispatchers.IO) {
            val userDao = userDatabase.userDao()
            userDao.insertAll()
        }
    }
}