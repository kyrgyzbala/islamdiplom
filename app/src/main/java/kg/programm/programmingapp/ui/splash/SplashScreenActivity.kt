package kg.programm.programmingapp.ui.splash

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kg.programm.programmingapp.MainActivity
import kg.programm.programmingapp.R
import kg.programm.programmingapp.databinding.ActivitySplashScreenBinding
import kg.programm.programmingapp.ui.login.LoginActivity
import kg.programm.programmingapp.ui.login.setup.SetupProfileActivity
import kg.programm.programmingapp.util.APP_LANG
import kg.programm.programmingapp.util.APP_LANG_KEY
import kg.programm.programmingapp.util.toast
import java.util.*

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = getSharedPreferences(APP_LANG, Context.MODE_PRIVATE)
        val lang = sp.getString(APP_LANG_KEY, "ru") ?: "ru"
        setLocale(lang)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        delayFor()
    }

    private fun delayFor() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            goToLogin()
        } else {
            checkIfProfileDone(user)
        }
    }

    private fun checkIfProfileDone(user: FirebaseUser) {
        FirebaseFirestore.getInstance().collection("users")
            .document(user.uid).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val isProfileDone = it.result?.getBoolean("profileDone")
                    if (isProfileDone == null || isProfileDone == false) {
                        goToProfile()
                    } else {
                        goToMain()
                    }
                } else {
                    toast(getString(R.string.errorTryAgain) + " ${it.exception}")
                }
            }
    }

    private fun goToLogin() {
        Intent(this, LoginActivity::class.java).let { intent ->
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
            startActivity(intent)
        }
    }

    private fun goToProfile() {
        val intent = Intent(this, SetupProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
        startActivity(intent)
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
        startActivity(intent)
    }

    @Suppress("DEPRECATION")
    private fun setLocale(lang: String) {
        val locale = when (lang) {
            "ky" -> Locale("ky")
            else -> Locale("en")
        }

        Locale.setDefault(locale)

        val config = Configuration()

        if (Build.VERSION.SDK_INT >= 24) {
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
        } else
            config.setLocale(locale)
        baseContext.applicationContext.createConfigurationContext(config)
    }
}