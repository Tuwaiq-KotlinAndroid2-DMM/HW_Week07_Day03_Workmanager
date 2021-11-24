package com.raialz.homeworklocation

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.work.*
import com.raialz.homeworklocation.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityMainBinding.inflate(layoutInflater)

        getPrimation()

        //binding.button.setOnClickListener {



//            var data =Data.Builder()
//                .putInt("loop",25)
//            worker.setInputData(data.build())
//            var w=worker.build()

//                .enqueue(w)

//            WorkManager.getInstance(this).getWorkInfoByIdLiveData(w.id)
//                .observe(this, Observer { info ->
//                    if (info != null && info.state.isFinished) {
//                        val myResult = info.outputData.getString("name" )
//
//                        binding.textViewManger.text=myResult.toString()
//
//
//
//                    }
//                })


        // }
        setContentView(binding.root)



    }
    fun getPrimation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // show request permission dialog
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),1)

        }else{

            var worker = PeriodicWorkRequestBuilder<LocationWorker>(10,TimeUnit.SECONDS)
            var w=WorkManager.getInstance(this)
            w.enqueue(worker.build())
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            getPrimation()

        }else{

            AlertDialog.Builder(this).apply {
                title= "warning"
                setMessage("To access location go to Setting-> allow location service")
                setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {


                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }

                })
            }.show()
        }


    }
}