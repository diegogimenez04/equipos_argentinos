package com.example.equiposargentinos.api

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkerUtils {
    fun scheduleSynchronization(context: Context){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest =
            PeriodicWorkRequestBuilder<SyncWorkManager>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SyncWorkManager.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, syncRequest
        )
    }
}