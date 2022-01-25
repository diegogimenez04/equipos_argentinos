package com.example.equiposargentinos.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.equiposargentinos.Team
import com.example.equiposargentinos.api.ApiResponseStatus
import com.example.equiposargentinos.database.getDatabase
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus>
        get() = _status

    /*private var _fbList = MutableLiveData<MutableList<Team>>()
    val fbList: LiveData<MutableList<Team>> = _fbList*/
    private var _searchList = MutableLiveData<MutableList<Team>>()
    val searchList: LiveData<MutableList<Team>> = _searchList

    val fbList = repository.fbList

    init {
        reloadTeams()
    }

    private fun reloadTeams() {
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


    fun reloadTeamsWithName(name: String){
        viewModelScope.launch {
            _searchList.value = repository.fetchTeamsWithName(name)
        }
    }
}