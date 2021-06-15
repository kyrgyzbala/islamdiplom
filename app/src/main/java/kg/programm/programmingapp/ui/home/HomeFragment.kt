package kg.programm.programmingapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.programm.programmingapp.R
import kg.programm.programmingapp.databinding.FragmentHomeBinding
import kg.programm.programmingapp.ui.home.info.HomeInfoActivity
import kg.programm.programmingapp.ui.home.tasks.TasksActivity
import kg.programm.programmingapp.ui.home.test.TestsActivity


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.infoButton.setOnClickListener {
            startActivity(Intent(requireContext(), HomeInfoActivity::class.java))
        }

        binding.assignmentsButton.setOnClickListener {
            startActivity(Intent(requireContext(), TasksActivity::class.java))
        }

        binding.testsButton.setOnClickListener {
            startActivity(Intent(requireContext(), TestsActivity::class.java))
        }

    }

}