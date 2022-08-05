package com.example.mobile_postagram

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mobile_postagram.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    companion object {
        val FULLSCREEN_FRAGMENTS = listOf(
            R.id.loginFragment,
            R.id.emailSignInFragment,
            R.id.addPostFragment,
            R.id.registerEmailFragment,
            R.id.registerDetailFragment
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavController()

        val user = Firebase.auth.currentUser
        user?.let {
            // if user not exist in database
            Firebase.database.reference.child("users").child(it.uid).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists() || !snapshot.hasChildren()) {
                            navController.navigate(R.id.action_homeFragment_to_registerDetailFragment)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }
            )
        } ?: run {
            navController.navigate(R.id.action_homeFragment_to_loginFragment)
        }


        val startForImagePostResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    val bundle = bundleOf("imageUri" to fileUri.toString())
                    navController.navigate(R.id.addPostFragment, bundle)
                    Log.d("ImagePicker", fileUri.toString())
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

        binding.fab.setOnClickListener {
            ImagePicker.with(this).galleryOnly().createIntent {
                startForImagePostResult.launch(it)
            }
        }
    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            for (id in FULLSCREEN_FRAGMENTS) {
                if (destination.id == id) {
                    hideFab()
                    hideBottomAppBar()

                    return@addOnDestinationChangedListener
                }
            }
            showFab()
            showBottomAppBar()

        }
    }

    private fun initFirebaseAuth() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { res ->
            this.onSignInResult(res)
        }

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {

        }
    }

    private fun hideFab() {
        binding.fab.visibility = View.GONE
    }

    private fun showFab() {
        binding.fab.visibility = View.VISIBLE
    }

    private fun hideBottomAppBar() {
        binding.bottomAppBar.visibility = View.GONE
    }

    private fun showBottomAppBar() {
        binding.bottomAppBar.visibility = View.VISIBLE
    }

}