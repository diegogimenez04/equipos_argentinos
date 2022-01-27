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
import com.example.equiposargentinos.User
import com.example.equiposargentinos.api.ApiResponseStatus
import com.example.equiposargentinos.database.UserDatabase
import com.example.equiposargentinos.database.getDatabase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

    val auth = Firebase.auth
    val firebaseUser = auth.currentUser

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private var _searchList = MutableLiveData<MutableList<Team>>()
    val searchList: LiveData<MutableList<Team>> = _searchList

    val fbList = repository.fbList

    init {
        reloadTeams()
        reloadUser(firebaseUser!!.uid)
        logUsers()
    }

    fun handleFavorite(team: Team): Boolean {
        var isFav = false
        val favTms = _user.value?.favoritesTeams
        if (favTms != null) {
            isFav = checkFavs(team, favTms)
            if (!isFav) {
                addFavTeam(team)
            } else {
                substractFromFav(team)
            }
        } else {
            addFavTeam(team)
        }
        return isFav
    }

    private fun checkFavs(team: Team, favTms: MutableList<Team>): Boolean {
        var isFav = false
        for (i in favTms) {
            if (i == team && !isFav) {
                isFav = true
            }
        }
        return isFav
    }

    private fun logUsers() {
        viewModelScope.launch {
            repository.logUsers()
            Log.d(TAG, "ViewModelUser: "+ _user.toString())
        }
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
            _user.value?.favoritesTeams?.remove(team)
        } catch (e: Exception) {
            Log.d(TAG, "_favTeams value is null")
        }

    }

    fun reloadTeamsWithName(name: String){
        viewModelScope.launch {
            _searchList.value = repository.fetchTeamsWithName(name)
        }
    }

    fun addFavTeam(team: Team){
        _user.value?.favoritesTeams?.add(team)
        logUsers()
    }

    fun reloadUser(uid: String) {
        viewModelScope.launch {
            try {
                _user.value = repository.fetchUser(uid)
                if (_user.value == null) {
                    repository.insertUser(User(uid, null, null))
                }
            } catch (e: UnknownHostException) {
                Log.d("MAINVIEWMODEL", "No internet connection.", e)
            }
        }
    }
}