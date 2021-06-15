package kg.programm.programmingapp.ui.lecture

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.lectures.ModelLectureCats
import kg.programm.programmingapp.databinding.FragmentLecturesBinding
import kg.programm.programmingapp.ui.lecture.util.LectureCatsRecyclerViewAdapter
import kg.programm.programmingapp.util.EXTRA_LEC_CAT


class LecturesFragment : Fragment(), LectureCatsRecyclerViewAdapter.LectureCatsListener {

    private var _binding: FragmentLecturesBinding? = null
    private val binding: FragmentLecturesBinding get() = _binding!!

    private var adapter: LectureCatsRecyclerViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLecturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = FirebaseFirestore.getInstance().collection("lecturecats")
            .orderBy("id", Query.Direction.ASCENDING)
        val options: FirestoreRecyclerOptions<ModelLectureCats> =
            FirestoreRecyclerOptions.Builder<ModelLectureCats>()
                .setQuery(query, ModelLectureCats::class.java).build()
        adapter = LectureCatsRecyclerViewAdapter(options, this)
        binding.recyclerView.adapter = adapter

        adapter?.startListening()
    }

    override fun onResume() {
        super.onResume()
        adapter?.startListening()
    }

    override fun onLectureCatClick(modelLectureCats: ModelLectureCats) {
        Intent(requireContext(), LecturesActivity::class.java).let { intent ->
            intent.putExtra(EXTRA_LEC_CAT, modelLectureCats)
            startActivity(intent)
        }
    }

}