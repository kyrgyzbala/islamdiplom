package kg.programm.programmingapp.ui.lecture

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kg.programm.programmingapp.data.lectures.ModelLectureCats
import kg.programm.programmingapp.databinding.FragmentLecturesBinding
import kg.programm.programmingapp.ui.lecture.util.LectureCatsRecyclerViewAdapter
import kg.programm.programmingapp.util.EXTRA_LEC_CAT
import kg.programm.programmingapp.util.hide
import kg.programm.programmingapp.util.show

class LecturesFragment : Fragment(), LectureCatsRecyclerViewAdapter.LectureCatsListener {

    private var _binding: FragmentLecturesBinding? = null
    private val binding: FragmentLecturesBinding get() = _binding!!

    private var adapter: LectureCatsRecyclerViewAdapter? = null
    private val cats = mutableListOf<ModelLectureCats>()

    private var searchKey: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLecturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchKey = binding.searchEditText.text.toString().lowercase()
                if (searchKey.isEmpty())
                    binding.clearButton.hide()
                else
                    binding.clearButton.show()
                initRecyclerView()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.clearButton.setOnClickListener {
            binding.searchEditText.setText("")
        }

        val query = FirebaseFirestore.getInstance().collection("lecturecats")
            .orderBy("id", Query.Direction.ASCENDING)

        query.get().addOnSuccessListener {
            cats.clear()
            for (q in it) {
                cats.add(q.toObject(ModelLectureCats::class.java))
            }
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        adapter = LectureCatsRecyclerViewAdapter(this)
        binding.recyclerView.adapter = adapter
        if (searchKey.isEmpty())
            adapter?.submitList(cats)
        else {
            val newList = mutableListOf<ModelLectureCats>()
            for (f in cats) {
                if (f.name.lowercase().contains(searchKey))
                    newList.add(f)
            }
            adapter?.submitList(newList)
        }
    }

    override fun onLectureCatClick(modelLectureCats: ModelLectureCats) {
        Intent(requireContext(), LecturesActivity::class.java).let { intent ->
            intent.putExtra(EXTRA_LEC_CAT, modelLectureCats)
            startActivity(intent)
        }
    }

}