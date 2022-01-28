package com.example.equiposargentinos.main

import android.app.Application
import android.service.autofill.UserData
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.equiposargentinos.Team
import com.example.equiposargentinos.api.ApiResponseStatus
import com.example.equiposargentinos.database.UserDatabase
import com.example.equiposargentinos.database.getDatabase
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException

val TAG = MainViewModel::class.java.simpleName
class MainViewModel(application: Application): AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val userDatabase = Room.databaseBuilder(
        application,
        UserDatabase::class.java, "user-database"
    ).build()
    private val repository = MainRepository(database, userDatabase)

    private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus>
        get() = _status

    private val favTeamsToAdd = arrayListOf<Team>()
    private val _favTeams = MutableLiveData<MutableList<Team>>()
    val favTeams: LiveData<MutableList<Team>> = _favTeams

    /*private var _fbList = MutableLiveData<MutableList<Team>>()
    val fbList: LiveData<MutableList<Team>> = _fbList*/
    private var _searchList = MutableLiveData<MutableList<Team>>()
    val searchList: LiveData<MutableList<Team>> = _searchList

    val fbList = repository.fbList

    init {
        reloadTeams()
        reloadUser()
    }

    fun reloadTeams() {
        viewModelScope.launch {
            try {
                _status.value = ApiResponseStatus.LOADING
                //_fbList.value = repository.fetchTeams()
                repository.fetchTeams()
                _status.value = ApiResponseStatus.DONE
            } catch (e: UnknownHostException) {
                _status.value = ApiResponseStatus.NO_INTERNET_CONNECTION
                Log.d("MAINVIEWMODEL", "No internet connection.", e)
            }
        }
    }

    fun substractFromFav(team: Team){
        try {
            _favTeams.value?.remove(team)
        } catch (e: Exception) {
            Log.d(TAG, "_favTeams value is null")
        }

    }

    fun reloadTeamsWithName(name: String) {
        viewModelScope.launch {
            _searchList.value = repository.fetchTeamsWithName(name)
        }
    }

    fun addFavTeam(team: Team){
        favTeamsToAdd.add(team)
        _favTeams.value = favTeamsToAdd

    }

    fun reloadUser() {
        viewModelScope.launch {
            try {
                //_fbList.value = repository.fetchTeams()
                repository.fetchUsers()
            } catch (e: UnknownHostException) {
                Log.d("MAINVIEWMODEL", "No internet connection.", e)
            }
        }
    }
}