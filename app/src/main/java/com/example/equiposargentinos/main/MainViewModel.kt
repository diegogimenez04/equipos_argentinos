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
    val status: LiveData<ApiResponseStatus> = _status

    private var _searchList = MutableLiveData<MutableList<Team>>()
    val searchList: LiveData<MutableList<Team>> = _searchList

    val fbList = repository.fbList

    init {
        reloadTeams()
        reloadUser()
    }

    private fun reloadTeams() {
        viewModelScope.launch {
            try {
                _status.value = ApiResponseStatus.LOADING
                repository.fetchTeams()
                _status.value = ApiResponseStatus.DONE
            } catch (e: UnknownHostException) {
                _status.value = ApiResponseStatus.NO_INTERNET_CONNECTION
                Log.d(TAG, "No internet connection.", e)
            }
        }
    }

    fun reloadTeamsWithName(name: String) {
        viewModelScope.launch {
            _searchList.value = repository.fetchTeamsWithName(name)
        }
    }

    private fun reloadUser() {
        viewModelScope.launch {
            try {
                repository.fetchUsers()
            } catch (e: UnknownHostException) {
                Log.d(TAG, "No internet connection.", e)
            }
        }
    }
}