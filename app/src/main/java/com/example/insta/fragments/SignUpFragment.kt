package com.example.insta.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.example.insta.R
import com.example.insta.databinding.FragmentSignUpBinding
import com.example.insta.util.InstaApplication
import com.example.insta.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        auth = ((activity as FragmentActivity).application as InstaApplication).fAuth
        storage = ((activity as FragmentActivity).application as InstaApplication).fStorage
        database = ((activity as FragmentActivity).application as InstaApplication).fDatabase

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.userAvatarIv.setImageURI(it)
            imageUri = it
        }

        // call launcher for profile image
        binding.userAvatarIv.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.registerBtn.setOnClickListener {

            val fullName = binding.fullNameEdt.text.toString().trim()
            val email = binding.emailEdt.text.toString().trim()
            val pswd = binding.passwordEdt.text.toString()
            val cPswd = binding.confirmPasswordEdt.text.toString()

            val errorMsg: String

            //validation
            if (
                imageUri != null &&
                fullName.isNotEmpty() &&
                email.isNotEmpty() &&
                pswd.isNotEmpty() &&
                cPswd.isNotEmpty() &&
                pswd == cPswd
            ) {
                createUser(imageUri, fullName, email, pswd)
            } else if (imageUri == null) {
                binding.detailErrorMessageTv.visibility = View.VISIBLE
                errorMsg = "please upload image"
                binding.detailErrorMessageTv.text = errorMsg

            } else if (fullName.isEmpty()) {
                binding.fullNameEdt.error = "please enter your name"
            } else if (email.isEmpty()) {
                binding.emailEdt.error = "please enter your email"
            } else if (pswd.isEmpty()) {
                binding.passwordEdt.error = "please enter your password"
            } else if (cPswd.isEmpty()) {
                binding.confirmPasswordEdt.error = "please enter confirm password"
            } else if (pswd != cPswd) {
                val missMatchMessage = "password and confirm password miss match"
                binding.passwordEdt.error = missMatchMessage
                binding.confirmPasswordEdt.error = missMatchMessage
            } else {
                binding.detailErrorMessageTv.visibility = View.VISIBLE
                errorMsg = "Please enter your details"
                binding.detailErrorMessageTv.text = errorMsg
            }
        }

        binding.loginTxt.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    private fun createUser(imageUri: Uri?, fullName: String, email: String, pswd: String) {

        //user Created for authentication
        auth.createUserWithEmailAndPassword(email, pswd)
            .addOnCompleteListener { createUser ->

                if (createUser.isSuccessful) {

                    //profile pic stored in firebase storage
                    val ref = storage.reference.child("profileImages").child(auth.uid!!)

                    ref.putFile(imageUri!!).addOnCompleteListener { profileImageStored ->

                        if (profileImageStored.isSuccessful) {

                            //downloading profile image uri from firebase storage
                            ref.downloadUrl.addOnCompleteListener { downloadProfileImage ->

                                if (downloadProfileImage.isSuccessful) {

                                    val user = User(
                                        email,
                                        fullName,
                                        downloadProfileImage.result.toString()
                                    )

                                    // saving user to the firebase database
                                    database.reference.child(auth.uid!!)
                                        .setValue(user)

                                }
                                auth.signOut()

                                Toast.makeText(
                                    requireContext(),
                                    "Account Created",
                                    Toast.LENGTH_SHORT
                                ).show()

                                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                            }
                        }
                    }
                }
            }
    }
}