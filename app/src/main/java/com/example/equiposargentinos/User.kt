package com.example.equiposargentinos

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties @Entity @Parcelize
data class User(@PrimaryKey val userId: String, val username: String? = null,
                val favoritesTeams: ArrayList<Team>?): Parcelable

class FavoritesTeamsTypeConverter {
    @TypeConverter
    fun fromString(value: String): ArrayList<Team>?{
        val listType = object:TypeToken<ArrayList<Team>>(){}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: ArrayList<Team>?): String{
        return Gson().toJson(value)
    }
}