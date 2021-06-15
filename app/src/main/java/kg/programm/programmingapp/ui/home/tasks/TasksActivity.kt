package kg.programm.programmingapp.ui.home.tasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.task.ModelTask
import kg.programm.programmingapp.databinding.ActivityTasksBinding
import kg.programm.programmingapp.util.EXTRA_TASK

class TasksActivity : AppCompatActivity(), TasksRecyclerViewAdapter.TaskRecyclerListener {

    private lateinit var binding: ActivityTasksBinding

    private var adapter: TasksRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)

        binding.arrBack.setOnClickListener {
            onBackPressed()
        }

        val query = FirebaseFirestore.getInstance().collection("tasks")
        val options: FirestoreRecyclerOptions<ModelTask> =
            FirestoreRecyclerOptions.Builder<ModelTask>().setQuery(query, ModelTask::class.java)
                .build()
        adapter = TasksRecyclerViewAdapter(options, this)
        binding.recyclerView.adapter = adapter
        adapter?.startListening()
    }

    override fun onResume() {
        super.onResume()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onTaskClick(current: ModelTask) {
        Intent(this, TaskDetailActivity::class.java).let { intent ->
            intent.putExtra(EXTRA_TASK, current)
            startActivity(intent)
        }
    }
}