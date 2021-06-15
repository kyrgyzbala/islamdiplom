package kg.programm.programmingapp.ui.home.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.test.ModelQuestion
import kg.programm.programmingapp.data.test.ModelQuestions
import kg.programm.programmingapp.databinding.ActivityTestActualBinding
import kg.programm.programmingapp.util.*

class TestActualActivity : AppCompatActivity(), CustomShowResult.CustomShowResultListener {

    private lateinit var binding: ActivityTestActualBinding
    private val questions = mutableListOf<ModelQuestion>()

    private var dots: MutableList<TextView> = mutableListOf()

    private var ref: String = ""
    private var index = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestActualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purpleBack)

        ref = intent.getStringExtra(EXTRA_TEST_REF) ?: ""
        val name = intent.getStringExtra(EXTRA_TEST_NAME)

        binding.progressBar.show()

        binding.toolbar.title = name

        initQuestions()

        binding.arrBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initViewQuestions() {
        if (questions.isNotEmpty()) {
            binding.questionCountTextView.text = questions.size.toString()
            initUI()

            binding.buttonNext.setOnClickListener {
                if (index < questions.size - 1) {
                    index++
                    initUI()
                } else {
                    val dialog = CustomShowResult(this)
                    dialog.show(supportFragmentManager, "showResults")
                }
            }

            binding.buttonPrev.setOnClickListener {
                if (index > 0) {
                    index--
                    initUI()
                }
            }

            binding.buttonA.setOnClickListener {
                chooseA()
                questions[index].userAnswer = 1
                removeB()
                removeC()
                removeD()
                removeE()
            }
            binding.buttonB.setOnClickListener {
                chooseB()
                questions[index].userAnswer = 2
                removeA()
                removeC()
                removeD()
                removeE()
            }
            binding.buttonC.setOnClickListener {
                chooseC()
                questions[index].userAnswer = 3
                removeB()
                removeA()
                removeD()
                removeE()
            }
            binding.buttonD.setOnClickListener {
                chooseD()
                questions[index].userAnswer = 4
                removeB()
                removeC()
                removeA()
                removeE()
            }
            binding.buttonE.setOnClickListener {
                chooseE()
                questions[index].userAnswer = 5
                removeB()
                removeC()
                removeD()
                removeA()
            }
        }
    }

    private fun initUI() {
        removeA()
        removeB()
        removeC()
        removeD()
        removeE()

        binding.questionNoTextView.text = (index + 1).toString()

        if (index == 0)
            binding.buttonPrev.visibility = View.GONE
        else
            binding.buttonPrev.visibility = View.VISIBLE

        if (index == questions.size - 1)
            binding.buttonNext.text = getString(R.string.result)
        else
            binding.buttonNext.text = getString(R.string.next)

        binding.questionTextView.text = questions[index].question

        binding.variantATextView.text = questions[index].varA
        binding.variantBTextView.text = questions[index].varB
        binding.variantCTextView.text = questions[index].varC
        binding.variantDTextView.text = questions[index].varD
        if (!questions[index].varE.isNullOrEmpty()) {
            binding.variantETextView.text = questions[index].varE
            binding.buttonE.show()
        } else {
            binding.buttonE.hide()
        }

        if (questions[index].photo.isNullOrEmpty())
            binding.imageCardView.visibility = View.GONE
        else {
            binding.imageCardView.visibility = View.VISIBLE
            Glide.with(this).load(questions[index].photo)
                .error(ContextCompat.getDrawable(this, R.drawable.def))
                .into(binding.imageView)
        }

        if (questions[index].userAnswer != -1 && questions[index].userAnswer != null)
            alreadyAnswered(questions[index].userAnswer!!)


    }

    private fun alreadyAnswered(userAnswer: Int) {
        when (userAnswer) {
            1 -> {
                chooseA()
            }
            2 -> {
                chooseB()
            }
            3 -> {
                chooseC()
            }
            4 -> {
                chooseD()
            }
            5 -> {
                chooseE()
            }
        }
    }

    private fun chooseA() {
        binding.buttonA.setBackgroundResource(R.drawable.back_question_a)
        binding.variantATextView
            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
    }

    private fun chooseB() {
        binding.buttonB.setBackgroundResource(R.drawable.back_question_a)
        binding.variantBTextView
            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
    }

    private fun chooseC() {
        binding.buttonC.setBackgroundResource(R.drawable.back_question_a)
        binding.variantCTextView
            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
    }

    private fun chooseD() {
        binding.buttonD.setBackgroundResource(R.drawable.back_question_a)
        binding.variantDTextView
            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
    }

    private fun chooseE() {
        binding.buttonE.setBackgroundResource(R.drawable.back_question_a)
        binding.variantETextView
            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
    }

    private fun removeA() {
        binding.buttonA.setBackgroundResource(R.drawable.back_question_na)
        binding.variantATextView
            .setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun removeB() {
        binding.buttonB.setBackgroundResource(R.drawable.back_question_na)
        binding.variantBTextView
            .setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun removeC() {
        binding.buttonC.setBackgroundResource(R.drawable.back_question_na)
        binding.variantCTextView
            .setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun removeD() {
        binding.buttonD.setBackgroundResource(R.drawable.back_question_na)
        binding.variantDTextView
            .setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun removeE() {
        binding.buttonE.setBackgroundResource(R.drawable.back_question_na)
        binding.variantETextView
            .setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun initQuestions() {
        FirebaseFirestore.getInstance().document(ref)
            .collection("questions").get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    if (!it.result!!.isEmpty) {
                        for (sn in it.result!!) {
                            val question = sn.getString("question")
                            val varA = sn.getString("varA")
                            val varB = sn.getString("varB")
                            val varC = sn.getString("varC")
                            val varD = sn.getString("varD")
                            val varE = sn.getString("varE")
                            val answer = sn.getLong("answer")!!.toInt()
                            val description = sn.getString("description")
                            val photo = sn.getString("photo")

                            val model = ModelQuestion(
                                question!!, varA!!, varB!!,
                                varC!!, varD!!, varE,
                                answer, description, photo
                            )
                            model.userAnswer = -1
                            questions.add(model)
                        }
                        binding.progressBar.hide()
                        initViewQuestions()
                    } else {
                        toast("NO Questions! Empty")
                        binding.progressBar.hide()
                    }
                } else {
                    toast("ERROR ${it.exception}")
                    binding.progressBar.hide()
                }
            }
    }

    private fun createDots(position: Int, size: Int) {
        binding.linearLayoutForDots.removeAllViews()

        var i = 0
        while (i < size) {
            dots.add(TextView(this))
            if (i == position) {
                dots[i].setBackgroundResource(
                    R.drawable.back_count
                )
                dots[i].setTextColor(
                    ContextCompat.getColor(this, R.color.colorWhite)
                )
            } else {
                dots[i].setTextColor(
                    ContextCompat.getColor(this, R.color.black)
                )
                dots[i].setBackgroundResource(R.drawable.back_count_uns)
            }
            dots[i].text = (i + 1).toString()
            dots[i].textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dots[i].rightPaddingDp = 5F
            dots[i].leftPaddingDp = 5F
            params.setMargins(12, 0, 12, 0)
            binding.linearLayoutForDots.addView(dots[i], params)
            i++
        }
    }

    override fun onShowResultConfirmed() {
        val modelQuestions = ModelQuestions(questions)
        Intent(this, ResultsActivity::class.java).let { intent ->
            intent.putExtra(EXTRA_QUESTIONS, modelQuestions)
            finish()
            startActivity(intent)
        }
    }
}