package com.example.mobile_postagram.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_postagram.models.Post
import com.example.mobile_postagram.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    fun getPosts(): Flow<Pair<User, Post>> = callbackFlow {

        var valueSingleUserListener: ValueEventListener

        val valuePostListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { data ->
                    val post = data.getValue(Post::class.java)!!

                    valueSingleUserListener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            trySend(Pair(user!!, post))
                            Log.d("HomeViewModel", snapshot.value.toString())
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    }
                    Firebase.database.reference.child("users/${post.uploader}")
                        .addListenerForSingleValueEvent(valueSingleUserListener)
                }


            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        val postReference = Firebase.database.reference.child("posts")
        postReference.addValueEventListener(valuePostListener)

        awaitClose {
            postReference.removeEventListener(valuePostListener)
        }

    }

    fun getPosts(startAt: String): Flow<Pair<User, Post>> = callbackFlow {

        val valueSingleUserListener = object : ValueEventListener {
            lateinit var post: Post
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                trySend(Pair(user!!, post))
                Log.d("HomeViewModel", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        val valuePostListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { data ->
                    val post = data.getValue(Post::class.java)!!
                    valueSingleUserListener.post = post
                    Firebase.database.reference.child("users/${post.uploader}")
                        .addListenerForSingleValueEvent(valueSingleUserListener)
                }


            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        val postReference = Firebase.database.reference.child("posts").orderByKey().startAt(startAt)
        postReference.addValueEventListener(valuePostListener)

        awaitClose {
            postReference.removeEventListener(valuePostListener)
        }

    }
}