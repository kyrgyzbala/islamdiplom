package kg.programm.programmingapp.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kg.programm.programmingapp.R
import kg.programm.programmingapp.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser!!

        FirebaseFirestore.getInstance().collection("users")
            .document(user.uid).get().addOnSuccessListener {
                val userName = it.getString("userName")
                val phone = user.phoneNumber
                val userPhoto = it.getString("userPhoto")

                Glide.with(requireContext()).load(userPhoto)
                    .error(
                        ContextCompat.getDrawable(requireContext(), R.drawable.def)
                    ).into(binding.userAvatar)
                binding.userNameTextView.text = userName
                binding.phoneNumber.text = phone
            }

        binding.editProfileButton.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

    }

}