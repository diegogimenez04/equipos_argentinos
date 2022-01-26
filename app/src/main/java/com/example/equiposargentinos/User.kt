package com.example.equiposargentinos

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@IgnoreExtraProperties @Entity
data class User(@PrimaryKey val userId: String, val username: String? = null,
                val favoritesTeams: List<Team>)

class FavoritesTeamsTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<Team>{
        val listType = object:TypeToken<List<Team>>(){}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: List<Team>): String{
        return Gson().toJson(value)
    }
}