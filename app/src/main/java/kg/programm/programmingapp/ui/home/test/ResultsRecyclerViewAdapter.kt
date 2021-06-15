package kg.programm.programmingapp.ui.home.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.test.ModelQuestion
import kg.programm.programmingapp.databinding.RowTestResultBinding
import kg.programm.programmingapp.util.hide
import kg.programm.programmingapp.util.show

class ResultsRecyclerViewAdapter :
    ListAdapter<ModelQuestion, ResultsRecyclerViewAdapter.ViewHolderTR>(DIFF) {

    private var _binding: RowTestResultBinding? = null

    fun getItemAtPos(position: Int): ModelQuestion {
        return getItem(position)
    }

    inner class ViewHolderTR(private val binding: RowTestResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {

            val current = getItemAtPos(position)

            binding.questionTextView.text = current.question
            if (current.photo.isNullOrEmpty()) {
                binding.imageCardView.visibility = View.GONE
            } else {
                binding.imageCardView.visibility = View.VISIBLE
                Glide.with(binding.root).load(current.photo)
                    .error(
                        ContextCompat.getDrawable(binding.root.context, R.drawable.def)
                    ).into(binding.imageView)
            }

            binding.variantATextView.text = current.varA
            binding.variantBTextView.text = current.varB
            binding.variantCTextView.text = current.varC
            binding.variantDTextView.text = current.varD
            if (current.varE.isNullOrEmpty())
                binding.buttonE.hide()
            else {
                binding.buttonE.show()
                binding.variantETextView.text = current.varE
            }

            clearAll()
            when (current.answer) {
                1 -> aIsTrue()
                2 -> bIsTrue()
                3 -> cIsTrue()
                4 -> dIsTrue()
                else -> eIsTrue()
            }
            if (current.userAnswer != current.answer) {
                binding.emptyAnswer.text = binding.root.context.getString(R.string.wrongAnswer)
                when (current.userAnswer) {
                    1 -> wrongA()
                    2 -> wrongB()
                    3 -> wrongC()
                    4 -> wrongD()
                    5 -> wrongE()
                    else -> {
                        binding.emptyAnswer.text =
                            binding.root.context.getString(R.string.emptyAnswer)
                        binding.emptyAnswer.show()
                    }
                }
            }

        }

        private fun clearAll() {
            binding.emptyAnswer.hide()
            binding.emptyAnswer.text = binding.root.context.getString(R.string.trueAnswer)


            binding.buttonA.setBackgroundResource(R.drawable.back_question_na)
            binding.variantATextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))

            binding.buttonB.setBackgroundResource(R.drawable.back_question_na)
            binding.variantBTextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))

            binding.buttonC.setBackgroundResource(R.drawable.back_question_na)
            binding.variantCTextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))

            binding.buttonD.setBackgroundResource(R.drawable.back_question_na)
            binding.variantDTextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))

            binding.buttonE.setBackgroundResource(R.drawable.back_question_na)
            binding.variantETextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
        }

        private fun aIsTrue() {
            binding.buttonA.setBackgroundResource(R.drawable.back_question_true)
            binding.variantATextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }

        private fun bIsTrue() {
            binding.buttonB.setBackgroundResource(R.drawable.back_question_true)
            binding.variantBTextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }

        private fun cIsTrue() {
            binding.buttonC.setBackgroundResource(R.drawable.back_question_true)
            binding.variantCTextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }

        private fun dIsTrue() {
            binding.buttonD.setBackgroundResource(R.drawable.back_question_true)
            binding.variantDTextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }

        private fun eIsTrue() {
            binding.buttonE.show()
            binding.buttonE.setBackgroundResource(R.drawable.back_question_true)
            binding.variantETextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }

        private fun wrongA() {
            binding.buttonA.setBackgroundResource(R.drawable.back_question_wrong)
            binding.variantATextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }

        private fun wrongB() {
            binding.buttonB.setBackgroundResource(R.drawable.back_question_wrong)
            binding.variantBTextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }

        private fun wrongC() {
            binding.buttonC.setBackgroundResource(R.drawable.back_question_wrong)
            binding.variantCTextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }

        private fun wrongD() {
            binding.buttonD.setBackgroundResource(R.drawable.back_question_wrong)
            binding.variantDTextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }

        private fun wrongE() {
            binding.buttonE.show()
            binding.buttonE.setBackgroundResource(R.drawable.back_question_wrong)
            binding.variantETextView
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTR {
        _binding = RowTestResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderTR(_binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolderTR, position: Int) {
        holder.onBind(position)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelQuestion>() {
            override fun areItemsTheSame(oldItem: ModelQuestion, newItem: ModelQuestion): Boolean {
                return oldItem.question == newItem.question
            }

            override fun areContentsTheSame(
                oldItem: ModelQuestion,
                newItem: ModelQuestion
            ): Boolean {
                return oldItem.question == newItem.question
            }

        }
    }
}