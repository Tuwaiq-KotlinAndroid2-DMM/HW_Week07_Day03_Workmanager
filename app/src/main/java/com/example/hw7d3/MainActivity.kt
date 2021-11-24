package com.example.hw7d3

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.hw7d3.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()

        var binding = ActivityMainBinding.inflate(layoutInflater)
        var textView = ActivityMainBinding.inflate(layoutInflater)
        binding.button.setOnClickListener {
            var worker = OneTimeWorkRequestBuilder<LocationWorker>()
//            var data = Data.Builder()
//                .putInt("Loop", 25)
//            worker.setInputData(data.build())
            var w =worker.build()
            binding.textView.text
            WorkManager.getInstance(this)
                .enqueue(worker.build())
            getResult(this,this,w.id)
        }
        setContentView(binding.root)
    }

    fun checkPermission() {
        if ( ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                7
            )
        }
    }

    fun getResult(context: Context, owner: LifecycleOwner, id: UUID) {
        WorkManager.getInstance(context)
            .getWorkInfoByIdLiveData(id)
            .observe(owner, Observer {
                if (it.state == WorkInfo.State.SUCCEEDED) {
//                    val result = it.outputData.getInt(WORKER_RESULT_INT, 0)
                    // do something with result
                    Toast.makeText(this, "Location saved to database", Toast.LENGTH_SHORT).show()
                }
            })
    }

}