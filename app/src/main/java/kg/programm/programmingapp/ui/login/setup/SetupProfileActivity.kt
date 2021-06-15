package kg.programm.programmingapp.ui.login.setup

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kg.programm.programmingapp.MainActivity
import kg.programm.programmingapp.R
import kg.programm.programmingapp.databinding.ActivitySetupProfileBinding
import kg.programm.programmingapp.util.hide
import kg.programm.programmingapp.util.show
import kg.programm.programmingapp.util.toast
import java.util.*

class SetupProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupProfileBinding

    private var loadedImg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)

        binding.logoCardView.setOnClickListener {
            openImageChooser()
        }

        binding.uploadUserPhoto.setOnClickListener {
            openImageChooser()
        }

        binding.saveButton.setOnClickListener {
            if (checkFields())
                saveUserProfile()
        }

        addTextListener()
    }

    private fun saveUserProfile() {
        val user = FirebaseAuth.getInstance().currentUser!!
        val profileUpdates = UserProfileChangeRequest.Builder().let {
            it.displayName = binding.userNameEditText.text.toString()
            it.photoUri = Uri.parse(loadedImg)
            it.build()
        }

        user.updateProfile(profileUpdates).addOnSuccessListener {
            val map = mutableMapOf<String, Any>()
            map["uid"] = user.uid
            map["userName"] = binding.userNameEditText.text.toString()
            map["userPhoto"] = loadedImg
            map["profileDone"] = true

            FirebaseFirestore.getInstance().collection("users").document(user.uid)
                .set(map, SetOptions.merge()).addOnSuccessListener {
                    Intent(this, MainActivity::class.java).let { intent ->
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        finish()
                        startActivity(intent)
                    }
                }
        }
    }

    private fun addTextListener() {
        binding.userNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.userNameEditText.toString().isEmpty()) {
                    binding.userNameEditText.error = getString(R.string.requiredField)
                } else {
                    binding.userNameEditText.error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun checkFields(): Boolean {
        var ret = true
        if (binding.userNameEditText.toString().isEmpty()) {
            binding.userNameEditText.error = getString(R.string.requiredField)
            ret = false
        }

        if (binding.prBarImgLoading.visibility == View.VISIBLE) {
            toast(getString(R.string.waitForImageLoad))
            ret = false
        }

        return ret
    }

    private fun openImageChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        resultLauncher.launch(Intent.createChooser(intent, "Выберите фото профиля!"))
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val uri = result.data!!.data
                if (uri != null) {
                    binding.imgViewPhoto.visibility = View.VISIBLE
                    binding.imgViewAdd.visibility = View.GONE
                    binding.imgViewPhoto.setImageURI(uri)
                    binding.prBarImgLoading.show()
                    val ref =
                        FirebaseStorage.getInstance()
                            .getReference("logos/${Date().time}.jpg")
                    ref.putFile(uri).addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { log ->
                            loadedImg = log.toString()
                            binding.prBarImgLoading.hide()
                        }.addOnFailureListener {
                            binding.prBarImgLoading.hide()
                        }
                    }
                }
            }
        }
}