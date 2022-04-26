package kg.programm.programmingapp.ui.lecture.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.lectures.ModelLecture
import kg.programm.programmingapp.databinding.RowLectureBinding

class LecturesRecyclerViewAdapter(
    private val listener: LectureRecyclerListener
) : ListAdapter<ModelLecture, LecturesRecyclerViewAdapter.ViewHolderLR>(DIFF) {

    private var _binding: RowLectureBinding? = null

    inner class ViewHolderLR(private val binding: RowLectureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val current = getItem(position)
            Glide.with(binding.root).load(current.thumbnail)
                .error(ContextCompat.getDrawable(binding.root.context, R.drawable.def))
                .into(binding.thumbnailImgView)
            binding.nameTextView.text = current.name
            binding.description.text = current.description

            binding.root.setOnClickListener {
                listener.onLectureClick(current.link)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderLR {
        _binding = RowLectureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderLR(_binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolderLR, position: Int) {
        holder.onBind(position)
    }

    interface LectureRecyclerListener {
        fun onLectureClick(link: String)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelLecture>() {
            override fun areItemsTheSame(oldItem: ModelLecture, newItem: ModelLecture): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: ModelLecture, newItem: ModelLecture): Boolean {
                return oldItem.name == newItem.name
            }

        }
    }

}