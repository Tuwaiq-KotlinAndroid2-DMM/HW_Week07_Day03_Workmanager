package com.example.w7d3_workmanager

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.security.AccessControlContext
import java.util.*
const val WORKER_RESULT_INT = "WORKER_RESULT_INT"


class LocationWork(appContext: Context,workerParams: WorkerParameters):Worker(appContext,workerParams) {


    fun ShowLocation(){

        val lc= applicationContext.getSystemService()

    }

    override fun doWork(): Result {

        var n= inputData.getInt("loop",1)
        for(i in 1..n){
            println(Date())
            Thread.sleep(1000)
        }
        return Result.success(Data.Builder().putInt(WORKER_RESULT_INT, 123).build())

    }



}