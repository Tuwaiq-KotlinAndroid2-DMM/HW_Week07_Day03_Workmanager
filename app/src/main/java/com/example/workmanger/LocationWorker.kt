package com.example.workmanger

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

const val WORKER_RESULT_INT = "WORKER_RESULT_INT"
class LocationWorker(context: Context,workerParameters: WorkerParameters)
    :Worker(context,workerParameters) {
    override fun doWork(): Result {
        var n =inputData.getInt("loop",25)
        return Result.success( return Result.success(Data.Builder()
            .putInt(WORKER_RESULT_INT, 123).build()))
    }




}
