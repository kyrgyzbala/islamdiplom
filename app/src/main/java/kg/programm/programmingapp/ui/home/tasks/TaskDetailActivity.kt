package kg.programm.programmingapp.ui.home.tasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.task.ModelTask
import kg.programm.programmingapp.databinding.ActivityTaskDetailBinding
import kg.programm.programmingapp.util.EXTRA_TASK

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)

        val currentTask = intent.getParcelableExtra<ModelTask>(EXTRA_TASK) as ModelTask

        binding.titleTextView.text = currentTask.name

        Glide.with(this).load(currentTask.photo)
            .error(ContextCompat.getDrawable(this, R.drawable.def))
            .into(binding.imgView)

        binding.textView.text = currentTask.description

        binding.arrBack.setOnClickListener {
            onBackPressed()
        }

    }
}