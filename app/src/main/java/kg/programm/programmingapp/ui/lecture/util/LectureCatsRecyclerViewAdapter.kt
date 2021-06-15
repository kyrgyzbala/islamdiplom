package kg.programm.programmingapp.ui.lecture.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.lectures.ModelLectureCats
import kg.programm.programmingapp.databinding.RowLectureCatsBinding

class LectureCatsRecyclerViewAdapter(
    options: FirestoreRecyclerOptions<ModelLectureCats>,
    private val listener: LectureCatsListener
) : FirestoreRecyclerAdapter<ModelLectureCats, LectureCatsRecyclerViewAdapter.ViewHolderLC>(options) {

    private var _binding: RowLectureCatsBinding? = null

    inner class ViewHolderLC(private val binding: RowLectureCatsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(current: ModelLectureCats) {
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

    override fun onBindViewHolder(holder: ViewHolderLC, position: Int, model: ModelLectureCats) {
        holder.onBind(model)
    }

    interface LectureCatsListener {
        fun onLectureCatClick(modelLectureCats: ModelLectureCats)
    }

}