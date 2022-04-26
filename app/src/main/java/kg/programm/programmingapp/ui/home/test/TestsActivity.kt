package kg.programm.programmingapp.ui.home.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kg.programm.programmingapp.R
import kg.programm.programmingapp.data.test.ModelTest
import kg.programm.programmingapp.databinding.ActivityTestsBinding
import kg.programm.programmingapp.ui.login.LoginActivity
import kg.programm.programmingapp.util.EXTRA_TEST_NAME
import kg.programm.programmingapp.util.EXTRA_TEST_REF
import kg.programm.programmingapp.util.hide

class TestsActivity : AppCompatActivity(), TestRecyclerViewAdapter.TestRecyclerClickListener {

    private lateinit var binding: ActivityTestsBinding

    private var adapter: TestRecyclerViewAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)

        binding.arrBack.setOnClickListener {
            onBackPressed()
        }
        initRecyclerView()
        binding.swipeRefresh.setOnRefreshListener {
            initRecyclerView()
        }

    }

    override fun onTestClick(position: ModelTest) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            Intent(this, TestActualActivity::class.java).let { intent ->
                intent.putExtra(EXTRA_TEST_REF, position.ref)
                intent.putExtra(EXTRA_TEST_NAME, position.name)
                startActivity(intent)
            }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if (adapter == null) {
            initRecyclerView()
        } else {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        val query = FirebaseFirestore.getInstance().collection("tests")
            .orderBy("id", Query.Direction.ASCENDING)

        query.get().addOnSuccessListener {
            val tests = mutableListOf<ModelTest>()
            for (q in it) {
                val temp = q.toObject(ModelTest::class.java)
                temp.ref = q.reference.path
                tests.add(temp)
            }
            adapter = TestRecyclerViewAdapter(this)
            binding.recyclerView.adapter = adapter
            adapter?.submitList(tests)
            binding.progressBar.hide()
            binding.swipeRefresh.isRefreshing = false
        }

    }

}