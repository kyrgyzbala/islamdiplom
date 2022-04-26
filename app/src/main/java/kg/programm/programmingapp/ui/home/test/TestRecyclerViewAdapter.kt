package kg.programm.programmingapp.ui.home.test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.test.ModelTest
import kg.programm.programmingapp.databinding.RowTestBinding

class TestRecyclerViewAdapter(
    private val listener: TestRecyclerClickListener
) : ListAdapter<ModelTest, TestRecyclerViewAdapter.ViewHolderTest>(DIFF) {

    private var _binding: RowTestBinding? = null

    inner class ViewHolderTest(private val binding: RowTestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val current = getItem(position)
            val circularProgressDrawable = CircularProgressDrawable(binding.root.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(binding.root).load(current.icon)
                .placeholder(circularProgressDrawable)
                .into(binding.iconImageView)
            binding.nameTextView.text = current.name

            val noOfQuestions =
                "${binding.root.context.getString(R.string.nOfQuestions)}  ${current.questions}"
            binding.questionCountTextView.text = noOfQuestions

            binding.descriptionTextView.text = current.description

            binding.root.setOnClickListener {
                listener.onTestClick(current)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTest {
        _binding = RowTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderTest(_binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolderTest, position: Int) {
        holder.onBind(position)
    }

    interface TestRecyclerClickListener {
        fun onTestClick(position: ModelTest)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelTest>() {
            override fun areItemsTheSame(oldItem: ModelTest, newItem: ModelTest): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ModelTest, newItem: ModelTest): Boolean {
                return oldItem.name == newItem.name
            }

        }
    }

}