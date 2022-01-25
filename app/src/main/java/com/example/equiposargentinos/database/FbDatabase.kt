package com.example.equiposargentinos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.equiposargentinos.Team


@Database(entities = [Team::class], version = 1)
abstract class FbDatabase: RoomDatabase() {
    abstract val fbDao: FbDao
}

private lateinit var INSTANCE: FbDatabase

fun getDatabase(context: Context): FbDatabase{
    synchronized(FbDatabase::class.java) {
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                FbDatabase::class.java,
                "teams_db"
            ).build()
        }
        return INSTANCE
    }
}
