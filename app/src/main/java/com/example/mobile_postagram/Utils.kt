package com.example.mobile_postagram

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.palette.graphics.Palette
import com.example.mobile_postagram.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

fun Bitmap.dominantColor(): Int {
    val palette = Palette.Builder(this).generate()
    return palette.getDominantColor(Color.BLACK)
}

fun Bitmap.gradient(): IntArray {
    return IntArray(3)
}

fun Firebase.isUserDetailExist(navController: NavController, callback: () -> Unit): Boolean {
    var isExist = false
    this.database.reference.child("users").child(this.auth.currentUser!!.uid)
        .addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)!!
                    Log.d("isUserDetailExist", user.toString())
                    if (user.username.isNullOrBlank()|| user.displayName == null) {
                        isExist = false
                        navController.navigate(
                            R.id.registerDetailFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.registerDetailFragment, true)
                                .build())
                        return
                    }
                    callback()
                    isExist = true
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
        )
    return isExist
}