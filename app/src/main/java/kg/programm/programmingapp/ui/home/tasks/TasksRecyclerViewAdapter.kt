package kg.programm.programmingapp.ui.home.tasks

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
import kg.programm.programmingapp.data.task.ModelTask
import kg.programm.programmingapp.databinding.RowTaskBinding

class TasksRecyclerViewAdapter(
    private val listener: TaskRecyclerListener
) : ListAdapter<ModelTask, TasksRecyclerViewAdapter.ViewHolderT>(DIFF) {

    private var _binding: RowTaskBinding? = null

    inner class ViewHolderT(private val binding: RowTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {

            val current = getItem(position)

            Glide.with(binding.root).load(current.photo)
                .error(ContextCompat.getDrawable(binding.root.context, R.drawable.def))
                .into(binding.imgView)
            binding.nameTextView.text = current.name
            binding.descriptionTextView.text = if (current.description.length > 100)
                current.name.take(100)
            else
                current.description

            binding.root.setOnClickListener {
                listener.onTaskClick(current)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderT {
        _binding = RowTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderT(_binding!!)
    }


    interface TaskRecyclerListener {
        fun onTaskClick(current: ModelTask)
    }

    override fun onBindViewHolder(holder: ViewHolderT, position: Int) {
        holder.onBind(position)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelTask>() {
            override fun areItemsTheSame(oldItem: ModelTask, newItem: ModelTask): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: ModelTask, newItem: ModelTask): Boolean {
                return oldItem.name == newItem.name
            }

        }
    }

}