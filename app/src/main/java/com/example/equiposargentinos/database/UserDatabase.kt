package com.example.equiposargentinos.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.equiposargentinos.FavoritesTeamsTypeConverter
import com.example.equiposargentinos.User

@Database(entities = [User::class], version = 1)
@TypeConverters(FavoritesTeamsTypeConverter::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}