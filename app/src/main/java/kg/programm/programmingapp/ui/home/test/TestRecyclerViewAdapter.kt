package kg.programm.programmingapp.ui.home.test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.test.ModelTest
import kg.programm.programmingapp.databinding.RowTestBinding

class TestRecyclerViewAdapter(
    options: FirestoreRecyclerOptions<ModelTest>,
    private val listener: TestRecyclerClickListener
) : FirestoreRecyclerAdapter<ModelTest, TestRecyclerViewAdapter.ViewHolderTest>(options) {

    private var _binding: RowTestBinding? = null

    inner class ViewHolderTest(private val binding: RowTestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(current: ModelTest, position: Int) {

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
                listener.onTestClick(adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTest {
        _binding = RowTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderTest(_binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolderTest, position: Int, model: ModelTest) {
        holder.onBind(model, position)
    }

    interface TestRecyclerClickListener {
        fun onTestClick(position: Int)
    }

}