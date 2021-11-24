import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.WorkSource
import android.renderscript.ScriptGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.w7d3_workmanager.LocationWork
import com.example.w7d3_workmanager.WORKER_RESULT_INT
import com.example.w7d3_workmanager.databinding.ActivityMainBinding
//import com.example.W7d3Workmanager.databinding.ActivityMainBinding
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.button.setOnClickListener {
            var worker = OneTimeWorkRequestBuilder<LocationWork>()
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

    fun checkPermissionForLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.))

    }

}