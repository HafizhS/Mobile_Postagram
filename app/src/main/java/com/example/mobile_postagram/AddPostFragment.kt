package com.example.mobile_postagram

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mobile_postagram.databinding.FragmentAddPostBinding
import com.example.mobile_postagram.models.Post
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AddPostFragment : Fragment() {

    private val viewModel: AddPostViewModel by viewModels()
    private lateinit var binding: FragmentAddPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uri = Uri.parse(arguments?.getString("imageUri"))
        Glide.with(context!!)
            .load(uri)
            .apply { this.diskCacheStrategy(DiskCacheStrategy.NONE) }
            .fitCenter()
            .into(binding.ivPreview)

        binding.btnPost.setOnClickListener {
            val user = Firebase.auth.currentUser
            val fileName = "${user?.uid}-${System.currentTimeMillis()}"

            binding.btnPost.isClickable = false
            Firebase.storage.reference.child("posts/${fileName}")
                .putFile(uri)
                .addOnCompleteListener {
                    it.result.storage.downloadUrl.addOnCompleteListener { task ->
                        // Insert Posts
                        val postRef = Firebase.database.reference.child("posts").push()
                        Firebase.database.reference.child("posts").push()
                        val post = Post(postRef.key!!, user!!.uid, task.result.toString(), fileName)
                        postRef.setValue(post)

                        Firebase.database.reference
                            .child("users/${user.uid}/posts/${postRef.key}").setValue(true)

                        binding.btnPost.isClickable = true
                        findNavController().navigate(R.id.action_addPostFragment_to_homeFragment)
                    }
                }

        }
    }

}