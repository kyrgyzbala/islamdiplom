package kg.programm.programmingapp.ui.lecture.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.lectures.ModelLectureCats
import kg.programm.programmingapp.databinding.RowLectureCatsBinding

class LectureCatsRecyclerViewAdapter(
    private val listener: LectureCatsListener
) : ListAdapter<ModelLectureCats, LectureCatsRecyclerViewAdapter.ViewHolderLC>(DIFF) {

    private var _binding: RowLectureCatsBinding? = null

    inner class ViewHolderLC(private val binding: RowLectureCatsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val current = getItem(position)
            Glide.with(binding.root).load(current.icon)
                .error(ContextCompat.getDrawable(binding.root.context, R.drawable.def))
                .into(binding.iconImgView)
            binding.nameTextView.text = current.name

            binding.root.setOnClickListener {
                listener.onLectureCatClick(current)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderLC {
        _binding = RowLectureCatsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderLC(_binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolderLC, position: Int) {
        holder.onBind(position)
    }

    interface LectureCatsListener {
        fun onLectureCatClick(modelLectureCats: ModelLectureCats)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelLectureCats>() {
            override fun areItemsTheSame(
                oldItem: ModelLectureCats,
                newItem: ModelLectureCats
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ModelLectureCats,
                newItem: ModelLectureCats
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}