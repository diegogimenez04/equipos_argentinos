package com.example.equiposargentinos

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "teams") @Parcelize
data class Team(@PrimaryKey val idTeam: Int, val strTeam: String, val strTeamShort: String,
                val strStadium: String, val strStadiumThumb: String,
                val strStadiumLocation: String, val intStadiumCapacity: Int,
                val strWebsite: String, val strTeamBadge: String): Parcelable