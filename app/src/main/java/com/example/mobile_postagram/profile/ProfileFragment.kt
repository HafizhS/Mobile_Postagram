package com.example.mobile_postagram.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.mobile_postagram.R
import com.example.mobile_postagram.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter =  ProfilePostAdapter()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.getUser().collect {
                binding.tvUsername.text = it!!.username
                binding.tvDisplayName.text = it.displayName
                Glide.with(binding.root)
                    .load(it.avatar)
                    .fitCenter()
                    .into(binding.ivProfileAvatar)

            }
        }


        binding.recycler.layoutManager =
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)

        lifecycleScope.launch() {
            viewModel.getUploadedPost().collect {
                adapter.addPost(it)
            }

        }

        binding.recycler.adapter = adapter
        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

}