package kg.programm.programmingapp.ui.home.test

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.test.ModelQuestion
import kg.programm.programmingapp.data.test.ModelQuestions
import kg.programm.programmingapp.databinding.ActivityResultsBinding
import kg.programm.programmingapp.util.EXTRA_QUESTIONS

class ResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultsBinding
    private lateinit var questions: List<ModelQuestion>

    private lateinit var adapter: ResultsRecyclerViewAdapter

    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)

        val q = intent.getSerializableExtra(EXTRA_QUESTIONS) as ModelQuestions
        questions = q.questions

        adapter = ResultsRecyclerViewAdapter()
        countScore()
        binding.recyclerView.adapter = adapter
        adapter.submitList(questions)

        binding.arrBack.setOnClickListener {
            onBackPressed()
        }

        binding.doneButton.setOnClickListener {
            onBackPressed()
        }

        binding.buttonDone.setOnClickListener {
            onBackPressed()
        }
    }

    private fun countScore() {
        for (q in questions) {
            if (q.userAnswer == q.answer)
                score++
        }
        val result = "$score / ${questions.size}"
        binding.resultScoreTextView.text = result
    }
}