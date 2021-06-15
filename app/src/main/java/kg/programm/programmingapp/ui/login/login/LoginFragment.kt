package kg.programm.programmingapp.ui.login.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kg.programm.programmingapp.MainActivity
import kg.programm.programmingapp.R
import kg.programm.programmingapp.databinding.FragmentLoginBinding
import kg.programm.programmingapp.ui.login.setup.SetupProfileActivity
import kg.programm.programmingapp.util.*
import java.util.concurrent.TimeUnit


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    private var mCallbacksClient: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mResendingTokenClient: PhoneAuthProvider.ForceResendingToken? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ccpLogin.registerCarrierNumberEditText(binding.phoneEditText)

        initVars()
        binding.loginButton.setOnClickListener {
            val phoneNumber = binding.ccpLogin.fullNumberWithPlus
            if (binding.ccpLogin.isValidFullNumber) {
                binding.progressBar.show()
                val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(requireActivity())
                    .setCallbacks(mCallbacksClient!!)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }

    }

    private fun initVars() {
        mCallbacksClient = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(authCredential: PhoneAuthCredential) {
                binding.progressBar.hide()
                Log.d("LoginFragment", "onVerificationCompleted: Success")
                signInWithPhone(authCredential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("LoginFragment", "onVerificationFailed: ${p0.message}")
            }

            override fun onCodeSent(
                s: String,
                resendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, resendingToken)
                try {
                    binding.progressBar.hide()
                } catch (e: Exception) {
                }
                mResendingTokenClient = resendingToken
                val bundle = Bundle()
                bundle.putString(EXTRA_CODE_SENT, s)
                bundle.putString(EXTRA_PHONE, binding.ccpLogin.fullNumber)
                findNavController().navigate(
                    R.id.action_loginFragment_to_loginPhoneConfirmationFragment, bundle
                )
            }
        }
    }

    private fun signInWithPhone(authCredential: PhoneAuthCredential) {
        binding.progressBar.show()

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    hideKeyboard()
                    checkIfProfileDone(user)
                }
            } else {
                Log.d("LoginFragment", "signInWithPhone: ${it.exception}")
            }
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
}