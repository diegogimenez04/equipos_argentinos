package com.example.equiposargentinos.main

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.service.autofill.UserData
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.equiposargentinos.R
import com.example.equiposargentinos.Team
import com.example.equiposargentinos.api.ApiResponseStatus
import com.example.equiposargentinos.database.UserDatabase
import com.example.equiposargentinos.database.getDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    private val app = application

    private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus> = _status

    private var _searchList = MutableLiveData<MutableList<Team>>()
    val searchList: LiveData<MutableList<Team>> = _searchList

    private var _favTeams = MutableLiveData<MutableList<Team>>()
    val favTeams: LiveData<MutableList<Team>> = _favTeams

    val fbList = repository.fbList

    init {
        reloadTeams()
        Log.d("Persistence", "Teams reloaded")
        reloadUser()
    }

    private fun saveFav(pref: Boolean, btnFav: ImageView) {
        viewModelScope.launch {
            if (pref) {
                btnFav.setImageResource(R.drawable.ic_fav)
            } else {
                btnFav.setImageResource(R.drawable.ic_unfav)
            }
        }
    }

    fun handleFavorite(team: Team, btnFav: ImageView) {
        viewModelScope.launch {
            val tempList = arrayListOf<Team>()
            Log.d("Persistence", "Templist before: $tempList")
            if (!_favTeams.value.isNullOrEmpty()) {
                tempList.addAll(_favTeams.value!!)
                Log.d("Persistence", "Templist: $tempList")
                // If initialized I check that it is not duplicated
                if (duplicated(team, tempList)) {
                    Log.d("Persistence", "Deleting: " + team.strTeam)
                    tempList.remove(team)
                    _favTeams.value = tempList
                    saveFav(false, btnFav)
                } else {
                    Log.d("Persistence", "Adding: " + team.strTeam)
                    tempList.add(team)
                    _favTeams.value = tempList
                    saveFav(true, btnFav)
                }
            } else if (_favTeams.value.isNullOrEmpty()) {
                // If its not initialized or is empty I add the items
                Log.d("Persistence", "Creating: " + team.strTeam)
                tempList.add(team)
                _favTeams.value = tempList
                saveFav(true, btnFav)
            }
        }
    }

    private fun duplicated(team: Team, tempList: ArrayList<Team>): Boolean {
        for (i in tempList) {
            if (team == i) return true
        }
        return false
    }

    private fun reloadTeams() {
        viewModelScope.launch {
            try {
                _status.value = ApiResponseStatus.LOADING
                val sharedPrefs = app.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                val gsonList = sharedPrefs.getString(PREF_LIST, "")
                val listType = object: TypeToken<ArrayList<Team>>(){}.type
                _favTeams.value = Gson().fromJson(gsonList, listType)
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