package com.example.mobile_postagram.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_postagram.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val model: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding

    val user = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HomePostAdapter()
        binding.recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)



        lifecycleScope.launch {
            model.getPosts().collect {
                adapter.addData(it)
            }
        }

        binding.recycler.adapter = adapter

    }


}