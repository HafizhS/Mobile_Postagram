package com.example.mobile_postagram.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mobile_postagram.R
import com.example.mobile_postagram.databinding.FragmentEmailSignInBinding
import com.example.mobile_postagram.isUserDetailExist
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmailSignInFragment : Fragment() {


    private val viewModel: EmailSignInViewModel by viewModels()
    private lateinit var binding: FragmentEmailSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnSignIn.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful && Firebase.auth.currentUser != null) {
                    Firebase.isUserDetailExist(findNavController()) {
                        findNavController().navigate(R.id.action_emailSignInFragment_to_homeFragment)
                    }

                }
            }
        }
    }
}