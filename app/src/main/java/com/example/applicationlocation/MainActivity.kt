package com.example.applicationlocation


import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button=findViewById<Button>(R.id.button)
        textView=findViewById<TextView>(R.id.textView)

        button.setOnClickListener {

            checkPermissionForLocation()

        }
    }


    fun checkPermissionForLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {

            // show request permission dialog
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),1)

        }else{

            showLocation()
        }

    }


    @SuppressLint("MissingPermission")
    fun showLocation(){

        var locationManager =getSystemService(LOCATION_SERVICE) as? LocationManager


        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,
            object : LocationListener {
                override fun onLocationChanged(location: Location) {


                    textView.text="${location.latitude}   ${location.longitude}"
                }

            })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            showLocation()
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