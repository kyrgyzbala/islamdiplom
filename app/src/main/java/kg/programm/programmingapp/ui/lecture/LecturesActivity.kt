package kg.programm.programmingapp.ui.lecture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.lectures.ModelLecture
import kg.programm.programmingapp.data.lectures.ModelLectureCats
import kg.programm.programmingapp.databinding.ActivityLecturesBinding
import kg.programm.programmingapp.ui.lecture.util.LecturesRecyclerViewAdapter
import kg.programm.programmingapp.util.EXTRA_LEC_CAT
import kg.programm.programmingapp.util.EXTRA_VIDEO_URL

class LecturesActivity : AppCompatActivity(), LecturesRecyclerViewAdapter.LectureRecyclerListener {

    private lateinit var binding: ActivityLecturesBinding

    private var adapter: LecturesRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLecturesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)
        binding.arrBack.setOnClickListener {
            onBackPressed()
        }
        val category =
            intent.getParcelableExtra<ModelLectureCats>(EXTRA_LEC_CAT) as ModelLectureCats
        binding.titleTextView.text = category.name

        val query = FirebaseFirestore.getInstance().collection("lectures")
            .whereEqualTo("category", category.id)
            .orderBy("order", Query.Direction.ASCENDING)
        val options: FirestoreRecyclerOptions<ModelLecture> =
            FirestoreRecyclerOptions.Builder<ModelLecture>()
                .setQuery(query, ModelLecture::class.java).build()
        adapter = LecturesRecyclerViewAdapter(options, this)
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

    override fun onLectureClick(link: String) {
        Intent(this, VideoFullActivity::class.java).let { intent ->
            intent.putExtra(EXTRA_VIDEO_URL, link)
            startActivity(intent)
        }
    }
}