package com.example.equiposargentinos.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.equiposargentinos.Team
import com.example.equiposargentinos.api.ApiResponseStatus
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository = MainRepository()

    private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus>
        get() = _status

    private var _fbList = MutableLiveData<MutableList<Team>>()
    val fbList: LiveData<MutableList<Team>> = _fbList

    init {
        reloadTeams()
    }

    fun reloadTeams() {
        viewModelScope.launch {
            try {
                _status.value = ApiResponseStatus.LOADING
                _fbList.value = repository.fetchTeams()
                _status.value = ApiResponseStatus.DONE
            } catch (e: UnknownHostException) {
                _status.value = ApiResponseStatus.NO_INTERNET_CONNECTION
                Log.d("MAINVIEWMODEL", "No internet connection.", e)
            }
        }
    }
}