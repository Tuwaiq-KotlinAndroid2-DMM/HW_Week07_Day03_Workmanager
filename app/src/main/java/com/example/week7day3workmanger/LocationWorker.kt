package com.example.week7day3workmanger

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*

class LocationWorker (appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {

        var n=inputData.getInt("loop",1)



        for (i in 1..n){
            println(Date())
            Thread.sleep(1000)

        }


        return Result.success()
    }}
