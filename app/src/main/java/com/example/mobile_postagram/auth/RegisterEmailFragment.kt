package com.example.mobile_postagram.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobile_postagram.R
import com.example.mobile_postagram.databinding.FragmentRegisterEmailBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterEmailFragment : Fragment() {

    lateinit var binding: FragmentRegisterEmailBinding
    var isPassowrdCorrect = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtRetypePassword.addTextChangedListener {
//            Log.d("RegisterEmail","${it.toString()} == ${binding.edtPassword.text.toString()}")
            if (!it.toString().contentEquals(binding.edtPassword.text.toString())) {
                binding.textInputLayoutRePassowrd.isErrorEnabled = true
                binding.textInputLayoutRePassowrd.error = "Password tidak sama"
                isPassowrdCorrect = false
                return@addTextChangedListener
            }
            binding.textInputLayoutRePassowrd.isErrorEnabled = false
            binding.textInputLayoutRePassowrd.error = ""
            isPassowrdCorrect = true

        }


        binding.btnNext.setOnClickListener {
            if (!isPassowrdCorrect) {
                Toast.makeText(it.context, "Pastikan password benar", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val email = binding.edtEmail.text.toString()
            val password = binding.edtRetypePassword.text.toString()
            findNavController().navigate(R.id.action_registerEmailFragment_to_registerDetailFragment)
            Firebase.auth.createUserWithEmailAndPassword(email, password)
        }
    }

}