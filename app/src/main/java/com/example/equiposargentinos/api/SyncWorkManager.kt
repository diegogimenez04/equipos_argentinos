package com.example.equiposargentinos.api

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.equiposargentinos.database.getDatabase
import com.example.equiposargentinos.main.MainRepository

class SyncWorkManager(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "SyncWorkManager "
    }

    private val database = getDatabase(appContext)
    private val repository = MainRepository(database)

    override suspend fun doWork(): Result {
        repository.fetchTeams()

        return Result.success()
    }

}