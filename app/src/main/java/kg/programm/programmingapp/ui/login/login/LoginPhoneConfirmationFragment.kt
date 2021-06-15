package kg.programm.programmingapp.ui.login.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kg.programm.programmingapp.MainActivity
import kg.programm.programmingapp.R
import kg.programm.programmingapp.databinding.FragmentLoginBinding
import kg.programm.programmingapp.databinding.FragmentLoginPhoneConfirmationBinding
import kg.programm.programmingapp.ui.login.login.util.GenericKeyEvent
import kg.programm.programmingapp.ui.login.login.util.GenericTextWatcher
import kg.programm.programmingapp.ui.login.setup.SetupProfileActivity
import kg.programm.programmingapp.util.*


class LoginPhoneConfirmationFragment : Fragment() {

    private var _binding: FragmentLoginPhoneConfirmationBinding? = null
    private val binding get() = _binding!!
    private var codeSent: String? = null
    private var phoneNumber: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginPhoneConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codeSent = arguments?.getString(EXTRA_CODE_SENT, "")
        phoneNumber = arguments?.getString(EXTRA_PHONE, "") ?: ""

        binding.arrBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        addFirstListeners()
        addLastListeners()

    }

    private fun signInServer() {
        val user = FirebaseAuth.getInstance().currentUser!!
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
                    requireContext().toast(getString(R.string.errorTryAgain) + " ${it.exception}")
                }
            }
    }

    private fun goToProfile() {
        val intent = Intent(requireContext(), SetupProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        requireActivity().finish()
        startActivity(intent)
    }

    private fun goToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        requireActivity().finish()
        startActivity(intent)
    }

    private fun addLastListeners() {

        binding.editText6.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    signInServer()
                } else if (checkForCodeLen()) {
                    checkIfCodeIsTrue()
                }
            }
        })

        binding.editText5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    signInServer()
                } else if (checkForCodeLen()) {
                    checkIfCodeIsTrue()
                }
            }
        })

        binding.editText4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    signInServer()
                } else if (checkForCodeLen()) {
                    checkIfCodeIsTrue()
                }
            }
        })

        binding.editText3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    signInServer()
                } else if (checkForCodeLen()) {
                    checkIfCodeIsTrue()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.editText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    signInServer()
                } else if (checkForCodeLen()) {
                    checkIfCodeIsTrue()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.editText1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    signInServer()
                } else if (checkForCodeLen()) {
                    checkIfCodeIsTrue()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun checkIfCodeIsTrue() {
        val code = getCode()
        Log.d("NURIKO", "checkIfCodeIsTrue: Code: $code")
        val credential = PhoneAuthProvider.getCredential(
            codeSent!!,
            code
        )
        signInWithPhone(credential)
    }

    private fun signInWithPhone(credential: PhoneAuthCredential) {
        binding.progressBar.show()

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    hideKeyboard()
                    signInServer()
                }
            } else {
                Log.d("PhoneConfirmation", "signInWithPhone: ${it.exception}")
            }
        }
    }

    private fun getCode(): String {
        val code1: String = binding.editText1.text.toString()
        val code2: String = binding.editText2.text.toString()
        val code3: String = binding.editText3.text.toString()
        val code4: String = binding.editText4.text.toString()
        val code5: String = binding.editText5.text.toString()
        val code6: String = binding.editText6.text.toString()
        return code1 + code2 + code3 + code4 + code5 + code6
    }

    private fun addFirstListeners() {
        binding.editText1.addTextChangedListener(
            GenericTextWatcher(binding.editText1, binding.editText2)
        )
        binding.editText2.addTextChangedListener(
            GenericTextWatcher(binding.editText2, binding.editText3)
        )
        binding.editText3.addTextChangedListener(
            GenericTextWatcher(binding.editText3, binding.editText4)
        )
        binding.editText4.addTextChangedListener(
            GenericTextWatcher(binding.editText4, binding.editText5)
        )
        binding.editText5.addTextChangedListener(
            GenericTextWatcher(binding.editText5, binding.editText6)
        )
        binding.editText6.addTextChangedListener(GenericTextWatcher(binding.editText6, null))

//GenericKeyEvent here works for deleting the element and to switch back to previous EditText
//first parameter is the current EditText and second parameter is previous EditText
        binding.editText1.setOnKeyListener(GenericKeyEvent(binding.editText1, null))
        binding.editText2.setOnKeyListener(GenericKeyEvent(binding.editText2, binding.editText1))
        binding.editText3.setOnKeyListener(GenericKeyEvent(binding.editText3, binding.editText2))
        binding.editText4.setOnKeyListener(GenericKeyEvent(binding.editText4, binding.editText3))
        binding.editText5.setOnKeyListener(GenericKeyEvent(binding.editText5, binding.editText4))
        binding.editText6.setOnKeyListener(GenericKeyEvent(binding.editText6, binding.editText5))
    }

    private fun checkForCodeLen(): Boolean {
        var ret = true

        val code1: String = binding.editText1.text.toString()
        val code2: String = binding.editText2.text.toString()
        val code3: String = binding.editText3.text.toString()
        val code4: String = binding.editText4.text.toString()
        val code5: String = binding.editText5.text.toString()
        val code6: String = binding.editText6.text.toString()

        if (code1.isEmpty()) {
            ret = false
        }
        if (code2.isEmpty()) {
            ret = false
        }
        if (code3.isEmpty()) {
            ret = false
        }
        if (code4.isEmpty()) {
            ret = false
        }
        if (code5.isEmpty()) {
            ret = false
        }
        if (code6.isEmpty()) {
            ret = false
        }
        return ret
    }


}