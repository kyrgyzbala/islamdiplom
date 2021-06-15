package kg.programm.programmingapp.ui.home.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kg.programm.programmingapp.R
import kg.programm.programmingapp.databinding.CustomShowTestresultBinding

class CustomShowResult(
    private val listener: CustomShowResultListener
) : BottomSheetDialogFragment() {

    private var _binding: CustomShowTestresultBinding? = null
    private val binding: CustomShowTestresultBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomShowTestresultBinding.inflate(inflater, container, false)

        binding.topButton.setOnClickListener {
            dismiss()
        }

        binding.buttonConfirm.setOnClickListener {
            listener.onShowResultConfirmed()
            dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    interface CustomShowResultListener {
        fun onShowResultConfirmed()
    }

}