package com.twq.workmanagerhomework

import android.content.Context
import android.content.Intent
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.work.Worker
import androidx.work.WorkerParameters

import java.util.*

class LocationWorker (context: Context,workerParameters: WorkerParameters): Worker(context,workerParameters) {



    override fun doWork(): Result {
        for (i in 1..30){
            println(Date())
            Thread.sleep(1000)
        }
        return Result.success()
    }
}