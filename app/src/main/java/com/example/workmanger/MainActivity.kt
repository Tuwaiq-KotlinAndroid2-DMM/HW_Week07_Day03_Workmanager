package com.example.workmanger
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.WorkSource
import android.renderscript.ScriptGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.workmanger.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.button.setOnClickListener {
            var worker = OneTimeWorkRequestBuilder<LocationWorker>()
            var data = Data.Builder()
                .putInt("loop", 25)
            worker.setInputData(data.build())
            var w= worker.build()
            WorkManager.getInstance(this)
                .enqueue(w)

            getResult(this,this,w.id)

        }
        setContentView(binding.root)
    }

    fun getResult(context: Context, owner: LifecycleOwner, id: UUID) {
        WorkManager.getInstance(context)
            .getWorkInfoByIdLiveData(id)
            .observe(owner, Observer {
                if (it.state == WorkInfo.State.SUCCEEDED) {
                    val result = it.outputData.getInt(WORKER_RESULT_INT, 0)
                    binding.textView.text=result.toString()
                }
            })
    }
}