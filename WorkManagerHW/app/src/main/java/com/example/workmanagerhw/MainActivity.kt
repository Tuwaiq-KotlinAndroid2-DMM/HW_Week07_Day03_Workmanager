package com.example.workmanagerhw
import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.renderscript.ScriptGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.work.*
import com.example.workmanagerhw.databinding.ActivityMainBinding
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


       // var textView = findViewById<TextView>(R.id.textView)
        val binding = ActivityMainBinding.inflate(layoutInflater)


        binding.button.setOnClickListener {
            check()

        }
        setContentView(binding.root)


    }

    fun check(){
        var locationManager = getSystemService(LOCATION_SERVICE) as? LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),0
            )
        }

        else{
            val worker= PeriodicWorkRequestBuilder<LocationWorker>(10,TimeUnit.SECONDS).build()
            var wm = WorkManager.getInstance(this)


        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            check()
        }else{
            AlertDialog.Builder(this).apply {
                title = "Warning"
                setMessage("To access location allow the permission")
                setPositiveButton("Ok",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts("package",packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                })
            }.show()
        }
    }


}