package com.example.mobile_postagram.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mobile_postagram.models.Post
import com.example.mobile_postagram.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ProfileViewModel : ViewModel() {
    sealed class RealtimeDatabaseResult {
        class Success(val dataSnapshot: DataSnapshot) : RealtimeDatabaseResult()
        class Error(val error: DatabaseError) : RealtimeDatabaseResult()
    }

    fun logout() {
        Firebase.auth.signOut()
    }

    fun getUser(): Flow<User?> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(User::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                close()
            }
        }
        val uid = Firebase.auth.currentUser!!.uid
        val reference = Firebase.database.reference.child("users").child(uid)
        reference.addValueEventListener(valueEventListener)

        awaitClose {
            reference.removeEventListener(valueEventListener)
        }
    }

    fun getUploadedPost(): Flow<Post> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d("ProfileViewHolder", snapshot.value.toString())
                snapshot.children.forEach {
                    Log.d("ProfileViewHolder", it.value.toString())
                    it.getValue(Post::class.java)?.let { it1 -> trySend(it1) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        val uid = Firebase.auth.currentUser!!.uid
        val reference = Firebase.database.reference.child("posts").orderByChild("uploader").equalTo(uid)
        reference.addValueEventListener(valueEventListener)

        awaitClose {
            reference.removeEventListener(valueEventListener)
        }
    }
}