package com.example.mobile_postagram.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mobile_postagram.DicebearService
import com.example.mobile_postagram.R
import com.example.mobile_postagram.databinding.FragmentRegisterDetailBinding
import com.example.mobile_postagram.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterDetailFragment : Fragment() {


    lateinit var binding: FragmentRegisterDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var avatarUrl = DicebearService.getRandomAvatarURL()

        binding.btnRandomize.setOnClickListener {
            avatarUrl = DicebearService.getRandomAvatarURL()
            Glide.with(binding.root)
                .load(avatarUrl)
                .apply { diskCacheStrategy(DiskCacheStrategy.NONE) }
                .fitCenter()
                .into(binding.ivPreviewProfile)
        }
        binding.btnRegister.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val displayName = binding.edtDisplayName.text.toString()
            val uid = Firebase.auth.currentUser!!.uid

            val user = User(uid, avatarUrl, displayName, username)
            Firebase.database.reference.child("users").child(uid).setValue(user)
                .addOnCompleteListener {
                    findNavController().navigate(R.id.action_registerDetailFragment_to_homeFragment)
                }
        }
    }


}