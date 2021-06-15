package kg.programm.programmingapp.ui.home.info

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import kg.programm.programmingapp.R
import kg.programm.programmingapp.databinding.ActivityHomeInfoBinding

class HomeInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)

        binding.arrBack.setOnClickListener {
            onBackPressed()
        }

        FirebaseFirestore.getInstance().collection("infodis").document("info")
            .get().addOnSuccessListener {
                val infoText = it.getString("info")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.textView.text =
                        Html.fromHtml(infoText, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    @Suppress("DEPRECATION")
                    binding.textView.text = Html.fromHtml(infoText)
                }
                Linkify.addLinks(binding.textView, Linkify.ALL)
                binding.textView.movementMethod = LinkMovementMethod.getInstance()
            }

    }
}